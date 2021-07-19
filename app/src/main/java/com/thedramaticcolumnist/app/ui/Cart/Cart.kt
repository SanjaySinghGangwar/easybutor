package com.thedramaticcolumnist.app.ui.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase.myCart
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.CartBinding
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.CartViewHolder

class Cart : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private var _binding: CartBinding? = null
    private val bind get() = _binding!!

    companion object {
        fun newInstance() = Cart()
    }

    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = CartBinding.inflate(inflater, container, false)
        val root: View = bind.root

        val textView: TextView = bind.textView
        cartViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        showCartData()
    }

    private fun showCartData() {

        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myCart!!, ProductModel::class.java)
                .build()
        val recyclerAdapter =
            object : FirebaseRecyclerAdapter<ProductModel, CartViewHolder>(option) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): CartViewHolder {
                    val binding: CartItemLayoutBinding =
                        CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context),
                            parent,
                            false)
                    return CartViewHolder(requireContext(), binding)
                }

                override fun onBindViewHolder(
                    holder: CartViewHolder,
                    position: Int,
                    model: ProductModel,
                ) {
                   
                    holder.bind(model)
                    holder.remove.setOnClickListener {

                        myCart!!.child(getRef(position).key.toString()).removeValue()
                            .addOnSuccessListener {
                                mToast(requireContext(), "Removed")
                                notifyDataSetChanged()
                            }

                    }

                }


            }


        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    private fun initAllComponent() {

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