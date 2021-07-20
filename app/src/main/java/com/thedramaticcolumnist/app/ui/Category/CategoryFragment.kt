package com.thedramaticcolumnist.app.ui.Category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.databinding.CategoryFragmentBinding
import com.thedramaticcolumnist.app.databinding.CategoryLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.CategoryViewHolder
import com.thedramaticcolumnist.app.ui.home.HomeFragmentDirections


class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private var _binding: CategoryFragmentBinding? = null


    private val bind get() = _binding!!
    private lateinit var myRef: DatabaseReference
    lateinit var database: FirebaseDatabase

    companion object {
        fun newInstance() = CategoryFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        categoryViewModel =
            ViewModelProvider(this).get(CategoryViewModel::class.java)

        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        categoryViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        setUpCategory()
    }


    private fun initAllComponents() {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference
        bind.categoryRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

    }
    private fun setUpCategory() {
        showLoader()
        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myRef.child("Categories"), ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, CategoryViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): CategoryViewHolder {
                    val binding: CategoryLayoutBinding =
                        CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return CategoryViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: CategoryViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                    hideLoader()
                    holder.bind(model)
                    holder.card.setOnClickListener {
                        val action = CategoryFragmentDirections.actionNavCategoryToViewCategoryProducts(model.name.toString())
                        view?.findNavController()?.navigate(action)
                    }
                    /* holder.card.setOnClickListener {
                         //mToast(requireContext(), getRef(position).key.toString())
                         val action = ProductsFragmentDirections.productsToProductDetail(getRef(position).key.toString())
                         view?.findNavController()?.navigate(action)
                     }*/

                }


            }

        bind.categoryRecycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun showLoader() {
        if (bind.progressBar.visibility == View.GONE) {
            bind.progressBar.visibility = View.VISIBLE
        }
    }

    fun hideLoader() {
        if (bind.progressBar.visibility == View.VISIBLE) {
            bind.progressBar.visibility = View.GONE
        }
    }

}