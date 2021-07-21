package com.thedramaticcolumnist.app.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.isValidText
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ProductLayoutBinding
import com.thedramaticcolumnist.app.databinding.SearchBinding
import com.thedramaticcolumnist.app.ui.Products.ProductsViewHolder


class Search : Fragment(), View.OnClickListener {

    private var _binding: SearchBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SearchBinding.inflate(inflater, container, false)
        return bind.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
    }

    private fun initAllComponents() {
        bind.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        bind.search.setOnClickListener(this)
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
                .setQuery(productDatabase.orderByChild("product_name"), ProductModel::class.java)
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
                            SearchDirections.searchToProductDetail(getRef(position).key.toString())
                        view?.findNavController()?.navigate(action)
                    }

                }


            }

        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search -> {
                val term = bind.name.text.toString()
                if (isValidText(bind.name.text.toString().trim(), bind.name)) {
                    val option: FirebaseRecyclerOptions<ProductModel> =
                        FirebaseRecyclerOptions.Builder<ProductModel>()
                            .setQuery(productDatabase.orderByChild("product_name")
                                .startAt(term).endAt("\uf8ff"), ProductModel::class.java)
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
                                        SearchDirections.searchToProductDetail(getRef(position).key.toString())
                                    view?.findNavController()?.navigate(action)
                                }

                            }


                        }

                    bind.recycler.adapter = recyclerAdapter
                    recyclerAdapter.startListening()
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}