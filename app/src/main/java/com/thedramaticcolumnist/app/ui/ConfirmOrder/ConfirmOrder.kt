package com.thedramaticcolumnist.app.ui.ConfirmOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.mAddress
import com.thedramaticcolumnist.app.Database.mDatabase.myCart
import com.thedramaticcolumnist.app.Database.mDatabase.productDatabase
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.Model.cart
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.hideLoader
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.Utils.mUtils.showLoader
import com.thedramaticcolumnist.app.databinding.CartItemLayoutBinding
import com.thedramaticcolumnist.app.databinding.ConfirmOrderBinding
import com.thedramaticcolumnist.app.mViewHolder.CartViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class ConfirmOrder : Fragment(), View.OnClickListener {

    val args: ConfirmOrderArgs by navArgs()
    private var _binding: ConfirmOrderBinding? = null
    private val bind get() = _binding!!

    var city = ""
    var houseAdd = ""
    var pinCode = ""
    var roadAddress = ""
    var state = ""
    val arrayList = ArrayList<cart>()

    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, CartViewHolder>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ConfirmOrderBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        showCartItems()
        setAddress()

    }

    private fun showCartItems() {
        showLoader(bind.progressBar)
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
                    hideLoader(bind.progressBar)
                    holder.bind(model)
                    arrayList.add(cart(model.id, model.quantity))

                }
            }
        bind.recycler.adapter = recyclerAdapter
        recyclerAdapter.startListening()
    }

    private fun setAddress() {
        showLoader(bind.progressBar)
        mAddress!!.child(args.addressID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hideLoader(bind.progressBar)
                if (snapshot.hasChild("city")) {
                    city = snapshot.child("city").value.toString()
                }
                if (snapshot.hasChild("full_name")) {
                    bind.name.text = snapshot.child("full_name").value.toString()
                }
                if (snapshot.hasChild("house_address")) {
                    houseAdd = snapshot.child("house_address").value.toString()
                }
                if (snapshot.hasChild("phone")) {
                    bind.phone.text = snapshot.child("phone").value.toString()
                }
                if (snapshot.hasChild("pinCode")) {
                    pinCode = snapshot.child("pinCode").value.toString()
                }
                if (snapshot.hasChild("road_address")) {
                    roadAddress = snapshot.child("road_address").value.toString()
                }
                if (snapshot.hasChild("state")) {
                    state = snapshot.child("state").value.toString()
                }
                bind.address.text = "$houseAdd $roadAddress $city $state \n$pinCode"
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(requireContext(), error.message)
            }
        })
    }

    private fun initAllComponents() {
        bind.proceed.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.proceed -> {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        for (i in arrayList.indices) {
            CoroutineScope(IO).launch {
                productDatabase.child(arrayList[i].toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (postSnapshot in snapshot.children) {
                                mLog(postSnapshot.toString())
                                mToast(requireContext(), postSnapshot.toString())
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            mToast(requireContext(), error.message)
                        }
                    })
            }

        }
    }
}

