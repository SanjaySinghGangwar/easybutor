package com.thedramaticcolumnist.app.ui.Products.ProdutsBySeller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.thedramaticcolumnist.app.Database.mDatabase
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.Model.ProductdetailModel
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ProductsBySellerBinding
import com.thedramaticcolumnist.app.ui.Products.ProductsFragmentDirections
import com.thedramaticcolumnist.app.ui.Search.ProductsListAdapter

class ProductsBySeller : Fragment(), ProductsListAdapter.onClickListner,
    CategoryAdapter.onClickListner {

    private var _binding: ProductsBySellerBinding? = null
    private val bind get() = _binding!!
    private var productsListAdapter: ProductsListAdapter? = null
    private var categoryListAdapter: CategoryAdapter? = null
    private var productsList: ArrayList<ProductListModel> = ArrayList()
    private var tempProductsList: ArrayList<ProductListModel> = ArrayList()
    val args: ProductsBySellerArgs by navArgs()

    var categoryList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductsBySellerBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
    }

    private fun initAllComponents() {
        setUpProductRecyclerView()
        setUpCategoryRecyclerView()

    }

    private fun setUpCategoryRecyclerView() {
        bind.categoryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        bind.categoryRecycler.hasFixedSize()
        categoryListAdapter = CategoryAdapter(requireContext(), this)
        bind.categoryRecycler.adapter = categoryListAdapter
    }

    private fun setUpProductRecyclerView() {
        bind.productsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.productsRecycler.hasFixedSize()
        productsListAdapter = ProductsListAdapter(requireContext(), this)
        bind.productsRecycler.adapter = productsListAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        getProductList()
        getCategoryList()
    }

    private fun getCategoryList() {
        mDatabase.mDatabase.child("Categories")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoryList.clear()
                    categoryList.add("All")
                    for (data in snapshot.children) {
                        categoryList.add(data.child("name").value.toString())
                    }
                    mLog(Gson().toJson(categoryList))
                    categoryListAdapter?.setItems(categoryList)
                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(requireContext(), error.message)
                }

            })
    }


    private fun getProductList() {
        productDatabase.orderByChild("seller").equalTo(args.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productsList.clear()
                    for (data in snapshot.children) {
                        productsList.add(
                            ProductListModel(
                                data.ref.key.toString(),
                                data.getValue(ProductdetailModel::class.java)!!
                            )
                        )
                    }
                    setProductsOnRecycler(productsList)

                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(requireContext(), error.message)
                }
            })
    }


    override fun onItemSelect(id: String) {
        val action = ProductsFragmentDirections.productsToProductDetail(id)
        view?.findNavController()?.navigate(action)
    }

    override fun onCategorySelect(categoryName: String) {
        if (categoryName == "All") {
            if (productsList.isEmpty()) {
                bind.temp.visibility = VISIBLE
            } else {
                bind.temp.visibility = GONE
                productsListAdapter?.setItems(productsList)
            }
        } else {
            tempProductsList.clear()
            for (i in productsList.indices) {
                if (productsList[i].detail.category.toString() == categoryName) {
                    mLog(productsList[i].detail.category + "  ::  " + categoryName)
                    tempProductsList.add(productsList[i])
                }
                setProductsOnRecycler(tempProductsList)
            }
        }

    }

    private fun setProductsOnRecycler(list: ArrayList<ProductListModel>) {
        if (list.isEmpty()) {
            bind.temp.visibility = VISIBLE
            bind.productsRecycler.visibility = GONE
        } else {
            bind.productsRecycler.visibility = VISIBLE
            bind.temp.visibility = GONE
            productsListAdapter?.setItems(list)
        }
        mLog(Gson().toJson(list))
    }
}