package com.thedramaticcolumnist.app.ui.WishList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.thedramaticcolumnist.app.Database.mDatabase
import com.thedramaticcolumnist.app.Database.mDatabase.mWishList
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding
import com.thedramaticcolumnist.app.databinding.WishlistFragmentBinding
import com.thedramaticcolumnist.app.mViewHolder.CartViewHolder

class WishlistFragment : Fragment(), View.OnClickListener {

    private lateinit var wishlistViewModel: WishlistViewModel
    private var _binding: WishlistFragmentBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, CartViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        wishlistViewModel =
            ViewModelProvider(this).get(WishlistViewModel::class.java)

        _binding = WishlistFragmentBinding.inflate(inflater, container, false)
        val root: View = bind.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        showCartData()
    }

    private fun initAllComponent() {
        bind.clearCart.setOnClickListener(this)
    }


    private fun showCartData() {

        val option: FirebaseRecyclerOptions<ProductModel> =
            FirebaseRecyclerOptions.Builder<ProductModel>()
                .setQuery(mWishList!!, ProductModel::class.java)
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
                    bind.temp.visibility = View.INVISIBLE
                    bind.list.visibility = View.VISIBLE
                    holder.bind(model)
                    val node = getRef(position).key.toString()
                    holder.remove.setOnClickListener {
                        mWishList!!.child(node).removeValue()
                            .addOnSuccessListener {
                                mUtils.mToast(requireContext(), "Removed")
                                notifyDataSetChanged()
                            }
                    }
                }


            }


        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clearCart -> {
                mWishList?.removeValue()?.addOnSuccessListener {
                    mUtils.mToast(requireContext(), "Removed")
                }?.addOnFailureListener {
                    mUtils.mToast(requireContext(), it.message.toString())
                }
            }
            R.id.pay -> {
                view?.findNavController()?.navigate(R.id.cart_to_address)
            }
        }
    }
}