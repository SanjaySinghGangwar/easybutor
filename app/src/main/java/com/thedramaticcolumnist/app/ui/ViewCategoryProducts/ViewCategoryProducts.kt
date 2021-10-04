package com.thedramaticcolumnist.app.ui.ViewCategoryProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductListModel
import com.thedramaticcolumnist.app.Model.ProductdetailModel
import com.thedramaticcolumnist.app.Utils.mUtils
import com.thedramaticcolumnist.app.databinding.ViewCategoryProductsBinding
import com.thedramaticcolumnist.app.ui.Search.ProductsListAdapter


class ViewCategoryProducts : Fragment(), ProductsListAdapter.onClickListner {

    private var _binding: ViewCategoryProductsBinding? = null
    private val bind get() = _binding!!

    val args: ViewCategoryProductsArgs by navArgs()
    private var productsList: ArrayList<ProductListModel> = ArrayList()
    private var productsListAdapter: ProductsListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ViewCategoryProductsBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAllComponents()
        getDataFromServer()
        setUpToolbar()
    }

    private fun getDataFromServer() {
        productDatabase.orderByChild("category").equalTo(args.category)
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
                    mUtils.mLog("Products :: " + Gson().toJson(productsList))
                    productsListAdapter?.setItems(productsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    mUtils.mToast(requireContext(), error.message)
                }
            })
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title = args.category
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

    override fun onItemSelect(id: String) {
        val action =
            ViewCategoryProductsDirections.actionViewCategoryProductsToProductDetail(id)
        view?.findNavController()?.navigate(action)
    }


    /* private fun initRecycler() {
         val option: FirebaseRecyclerOptions<ProductModel> =
             FirebaseRecyclerOptions.Builder<ProductModel>()
                 .setQuery(
                     productDatabase.orderByChild("category").equalTo(args.category),
                     ProductModel::class.java
                 )
                 .build()
         val recyclerAdapter =
             object : FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>(option) {
                 override fun onCreateViewHolder(
                     parent: ViewGroup,
                     viewType: Int,
                 ): ProductsViewHolder {
                     val binding: ProductLayoutBinding =
                         ProductLayoutBinding.inflate(
                             LayoutInflater.from(parent.context),
                             parent,
                             false
                         )
                     return ProductsViewHolder(requireContext(), binding, listener)
                 }

                 override fun onBindViewHolder(
                     holder: ProductsViewHolder,
                     position: Int,
                     model: ProductModel,
                 ) {
                     holder.bind(model)
                     holder.card.setOnClickListener {
                         //mToast(requireContext(), getRef(position).key.toString())
                         val action =
                             ViewCategoryProductsDirections.actionViewCategoryProductsToProductDetail(
                                 getRef(position).key.toString()
                             )
                         view?.findNavController()?.navigate(action)
                     }

                 }


             }

         bind.recycler.adapter = recyclerAdapter
         recyclerAdapter.startListening()
     }*/
}