package com.thedramaticcolumnist.app.ui.WishList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.mWishList
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Model.WishListDetailsModel
import com.thedramaticcolumnist.app.Model.WishListModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.adapter.WishListAdapter
import com.thedramaticcolumnist.app.databinding.WishlistFragmentBinding
import com.thedramaticcolumnist.app.mViewHolder.WishListViewHolder

class WishlistFragment : Fragment(), View.OnClickListener, WishListAdapter.onClickListner {

    private lateinit var wishlistViewModel: WishlistViewModel
    private var _binding: WishlistFragmentBinding? = null
    private val bind get() = _binding!!
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, WishListViewHolder>


    private var wishListAdapter: WishListAdapter? = null
    private var wishList: ArrayList<WishListModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        wishlistViewModel =
            ViewModelProvider(this).get(WishlistViewModel::class.java)

        _binding = WishlistFragmentBinding.inflate(inflater, container, false)


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        setUpRecyclerView()
        setUpWishList()
    }

    private fun setUpRecyclerView() {
        bind.recycler.layoutManager = LinearLayoutManager(requireContext())
        bind.recycler.hasFixedSize()
        wishListAdapter = WishListAdapter(requireContext(), this)
        bind.recycler.adapter = wishListAdapter
    }

    private fun setUpWishList() {
        mWishList?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wishList.clear()

                for (data in snapshot.children) {
                    wishList.add(
                        WishListModel(
                            data.ref.key.toString(),
                            data.getValue(WishListDetailsModel::class.java)!!
                        )
                    )
                }
                if (wishList.size > 0) {
                    wishListAdapter!!.setItems(wishList)
                    bind.temp.visibility = View.INVISIBLE
                    bind.list.visibility = View.VISIBLE
                } else {
                    bind.temp.visibility = View.VISIBLE
                    bind.list.visibility = View.INVISIBLE
                }


            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }

        })


    }

    private fun initAllComponent() {
        bind.clearCart.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clearCart -> {
                mWishList?.removeValue()?.addOnSuccessListener {
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

    override fun onRemoveCLick(id: String) {
        mWishList?.child(id)?.removeValue()?.addOnSuccessListener {
            mToast(requireContext(), "Removed !!")
        }?.addOnFailureListener {
            mToast(requireContext(), it.message.toString())
        }
    }

    override fun onQuantityUpdate(id: String, quantity: String) {
        mWishList?.child(id)?.child("quantity")?.setValue(quantity)
            ?.addOnSuccessListener { }
            ?.addOnFailureListener {
                mToast(requireContext(), it.message.toString())
            }
    }
}