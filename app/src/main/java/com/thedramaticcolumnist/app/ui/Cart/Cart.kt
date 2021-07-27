package com.thedramaticcolumnist.app.ui.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase.myCart
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Model.cart
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.CartBinding
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding
import com.thedramaticcolumnist.app.mViewHolder.CartViewHolder


class Cart : Fragment(), View.OnClickListener {

    private var quantity: String = "1"
    private lateinit var cartViewModel: CartViewModel
    private var _binding: CartBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, CartViewHolder>

    var cartList = ArrayList<cart>()

    companion object {
        fun newInstance() = Cart()
    }

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
        showLoader()
        initAllComponent()
        showCartData()
    }

    private fun showCartData() {

        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(myCart!!, ProductModel::class.java)
                .build()
        recyclerAdapter =
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
                    hideLoader()
                    holder.bind(model)
                    cartList.add(cart(model.id, model.quantity))
                    val node = getRef(position).key.toString()
                    holder.remove.setOnClickListener {
                        myCart!!.child(node).removeValue()
                            .addOnSuccessListener {
                                mToast(requireContext(), "Removed")
                                notifyDataSetChanged()
                            }

                    }
                    holder.saveForLater.setOnClickListener {
                        myCart?.child(node)?.child("quantity")
                            ?.setValue(getQuantity())
                            ?.addOnFailureListener {
                                mToast(requireContext(), it.message.toString())
                            }?.addOnSuccessListener {
                                mToast(requireContext(), "added")
                            }
                    }


                }


            }


        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()

    }

    private fun getQuantity(): String {
        return quantity
    }

    private fun initAllComponent() {
        bind.clearCart.setOnClickListener(this)
        bind.pay.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clearCart -> {
                myCart?.removeValue()?.addOnSuccessListener {
                    mToast(requireContext(), "Removed")
                }?.addOnFailureListener {
                    mToast(requireContext(), it.message.toString())
                }
            }
            R.id.pay -> {
                view?.findNavController()?.navigate(R.id.cart_to_address)
            }
        }
    }
}