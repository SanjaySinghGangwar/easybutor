package com.thedramaticcolumnist.app.ui.Products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.Model.ProductdetailModel
import com.thedramaticcolumnist.app.Utils.mUtils
import com.thedramaticcolumnist.app.databinding.ProductsFragmentBinding
import com.thedramaticcolumnist.app.ui.Search.ProductsListAdapter

class ProductsFragment : Fragment(), ProductsListAdapter.onClickListner {

    private lateinit var productsAccountViewModel: ProductsViewModel
    private var _binding: ProductsFragmentBinding? = null
    private val bind get() = _binding!!
    private var productsListAdapter: ProductsListAdapter? = null
    private var productsList: ArrayList<ProductListModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        productsAccountViewModel =
            ViewModelProvider(this).get(ProductsViewModel::class.java)
        _binding = ProductsFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
    }

    private fun initAllComponents() {
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.recycler.hasFixedSize()
        productsListAdapter = ProductsListAdapter(requireContext(), this)
        bind.recycler.adapter = productsListAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        getProductList()

    }

    private fun getProductList() {
        productDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
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
                if (productsList.isEmpty()) {
                    bind.temp.visibility = VISIBLE
                } else {
                    bind.temp.visibility = GONE
                    productsListAdapter?.setItems(productsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mUtils.mToast(requireContext(), error.message)
            }
        })
    }

    override fun onItemSelect(id: String) {
        val action = ProductsFragmentDirections.productsToProductDetail(id)
        view?.findNavController()?.navigate(action)
    }

}