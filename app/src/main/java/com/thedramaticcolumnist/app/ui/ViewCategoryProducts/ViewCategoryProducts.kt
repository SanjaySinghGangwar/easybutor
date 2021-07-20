package com.thedramaticcolumnist.app.ui.ViewCategoryProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.app.databinding.ViewCategoryProductsBinding
import com.thedramaticcolumnist.app.ui.Products.ProductsFragmentDirections
import com.thedramaticcolumnist.app.ui.Products.ProductsViewHolder


class ViewCategoryProducts : Fragment() {

    private var _binding: ViewCategoryProductsBinding? = null
    private val bind get() = _binding!!

    val args: ViewCategoryProductsArgs by navArgs()

    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase


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

    }


    private fun initAllComponents() {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference.child("Products")
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        initRecycler()

    }

    private fun initRecycler() {
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myRef.orderByChild("category").equalTo(args.category), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, ProductsViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): ProductsViewHolder {
                    val binding: ProductLayoutBinding =
                        ProductLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return ProductsViewHolder(requireContext(), binding)
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
                            ViewCategoryProductsDirections.actionViewCategoryProductsToProductDetail(getRef(position).key.toString())
                        view?.findNavController()?.navigate(action)
                    }

                }


            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }
}