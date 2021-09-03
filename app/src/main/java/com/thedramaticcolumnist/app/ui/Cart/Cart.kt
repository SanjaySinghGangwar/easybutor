package com.thedramaticcolumnist.app.ui.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.myCart
import com.thedramaticcolumnist.app.Model.CartListDetailsModel
import com.thedramaticcolumnist.app.Model.CartListModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.adapter.CartListAdapter
import com.thedramaticcolumnist.app.databinding.CartBinding


class Cart : Fragment(), View.OnClickListener, CartListAdapter.onClickListner {

    private lateinit var cartViewModel: CartViewModel
    private var _binding: CartBinding? = null
    private val bind get() = _binding!!


    private var cartListAdapter: CartListAdapter? = null
    private var carList: ArrayList<CartListModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        cartViewModel =
            ViewModelProvider(this).get(CartViewModel::class.java)

        _binding = CartBinding.inflate(inflater, container, false)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        //showCartData()
        setUpRecyclerView()
        setUpCartList()
    }

    private fun setUpCartList() {
        myCart?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                carList.clear()

                for (data in snapshot.children) {
                    carList.add(
                        CartListModel(
                            data.ref.key.toString(),
                            data.getValue(CartListDetailsModel::class.java)!!
                        )
                    )
                }
                if (carList.size > 0) {
                    cartListAdapter!!.setItems(carList)
                    bind.temp.visibility = INVISIBLE
                    bind.list.visibility = VISIBLE
                } else {
                    bind.temp.visibility = VISIBLE
                    bind.list.visibility = INVISIBLE
                }


            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }

        })

    }

    private fun setUpRecyclerView() {
        bind.recycler.layoutManager = LinearLayoutManager(requireContext())
        bind.recycler.hasFixedSize()
        cartListAdapter = CartListAdapter(requireContext(), this)
        bind.recycler.adapter = cartListAdapter
    }


    private fun initAllComponent() {
        bind.clearCart.setOnClickListener(this)
        bind.pay.setOnClickListener(this)
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

    override fun onRemoveCLick(id: String) {
        myCart?.child(id)?.removeValue()?.addOnSuccessListener {
            mToast(requireContext(), "Removed !!")
        }?.addOnFailureListener {
            mToast(requireContext(), it.message.toString())
        }
    }

    override fun onQuantityUpdate(id: String, quantity: String) {
        myCart?.child(id)?.child("quantity")?.setValue(quantity)
            ?.addOnSuccessListener {

            }?.addOnFailureListener {
                mToast(requireContext(), it.message.toString())
            }
    }

}