package com.thedramaticcolumnist.app.ui.Products.ViewProduct

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.*
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.ProductDetailBinding


class ProductDetail : Fragment() {

    val args: ProductDetailArgs by navArgs()
    lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var _binding: ProductDetailBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ProductDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
        fetchProductDetail()
        //mToast(requireContext(), args.productID)
    }

    private fun initAllComponent() {
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Products")
            .child(args.productID)
    }

    private fun fetchProductDetail() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("category")) {

                }
                if (dataSnapshot.hasChild("image")) {
                    mLog(dataSnapshot.child("image").value.toString())
                }

                if (dataSnapshot.hasChild("long_description")) {
                    bind.longDescription.text =
                        dataSnapshot.child("long_description").value.toString()
                }
                if (dataSnapshot.hasChild("price")) {
                  bind.price.text = "₹ "+ dataSnapshot.child("price").value.toString()
                }
                if (dataSnapshot.hasChild("product_name")) {
                    bind.name.text = dataSnapshot.child("product_name").value.toString()


                }
                if (dataSnapshot.hasChild("seller")) {
                }
                if (dataSnapshot.hasChild("short_description")) {
                    bind.shortDescription.text =
                        dataSnapshot.child("short_description").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mToast(
                    context?.applicationContext!!,
                    error.toException().message.toString()
                )
            }
        })
    }
}