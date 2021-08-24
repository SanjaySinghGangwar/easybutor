package com.thedramaticcolumnist.app.ui.orderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.Database.mDatabase.myOrder
import com.thedramaticcolumnist.app.Model.ProductModel
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.OrderDetailBinding
import com.thedramaticcolumnist.app.mViewHolder.OrderDetailViewHolder


class OrderDetail : Fragment() {

    private var _binding: OrderDetailBinding? = null
    private val bind get() = _binding!!
    val args: OrderDetailArgs by navArgs()
    lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ProductModel, OrderDetailViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = OrderDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponents()
        showOrderDetail()
        fetchRealTimeStatusOfOrder()
    }

    private fun showOrderDetail() {
        myOrder.child(args.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bind.address.text = snapshot.child("address").value.toString()
                bind.price.text = snapshot.child("price").value.toString()
                bind.date.text = snapshot.child("date").value.toString()
                bind.name.text = snapshot.child("product_name").value.toString()
                bind.orderID.text = snapshot.child("orderId").value.toString()
                bind.quantity.text = snapshot.child("quantity").value.toString()
                bind.sellerID.text = snapshot.child("seller").value.toString()
                bind.shortDescription.text = snapshot.child("short_description").value.toString()
                Glide.with(requireContext())
                    .load(snapshot.child("image_one").value.toString())
                    .placeholder(R.drawable.ic_default_product)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_error)
                    .into(bind.image);
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun fetchRealTimeStatusOfOrder() {
        try {
            myOrder.child(args.id).child("Status")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild("flag")) {
                            if (snapshot.hasChild("flag"))
                                bind.status.text = snapshot.child("flag").value.toString()

                            if (snapshot.child("flag").value.toString() == "Waiting for Shipment detail") {
                                bind.trackingLayout.visibility = View.VISIBLE
                                bind.shippedLayout.visibility = View.VISIBLE
                                bind.buttonText.text = "UPDATE"
                            } else if (snapshot.child("flag").value.toString() == "In-Transit") {
                                bind.trackingLayout.visibility = View.VISIBLE
                                bind.shippedLayout.visibility = View.VISIBLE
                                bind.buttonText.text = "UPDATE"

                                bind.trackingNumber.setText(snapshot.child("trackingNumber").value.toString())
                                bind.companyName.setText(snapshot.child("companyName").value.toString())
                            }

                        } else {
                            bind.status.text = "Waiting for approval"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        mToast(requireContext(), error.message)
                    }

                })
        } catch (e: Exception) {
            mLog("STATUS IS NOT THERE")
        }
    }
    private fun initAllComponents() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}