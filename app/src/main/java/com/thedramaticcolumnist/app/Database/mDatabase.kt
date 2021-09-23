package com.thedramaticcolumnist.app.Database

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thedramaticcolumnist.app.FCM.Notification.sendNotification
import com.thedramaticcolumnist.app.Utils.mUtils.mLog
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import java.text.SimpleDateFormat
import java.util.*


object mDatabase {
    /*var chatMessageDataBase = FirebaseStorage.getInstance().reference.child("chatData")
    var UserDatabase = mDatabase.child("USERs")*/


    var mDatabase = FirebaseDatabase.getInstance().reference

    var myCart = FirebaseAuth.getInstance().currentUser?.uid?.let {
        mDatabase.child("Easybutor")
            .child(it).child("cart")
    }

    var mWishList = FirebaseAuth.getInstance().currentUser?.uid?.let {
        mDatabase.child("Easybutor")
            .child(it).child("wishlist")
    }

    var myProfile = FirebaseAuth.getInstance().currentUser?.uid?.let {
        mDatabase.child("Easybutor")
            .child(it)
    }

    var uID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    var productDatabase = mDatabase.child("Products")

    var mAddress = myProfile?.child("address")

    fun createOrder(hash: HashMap<String, String>, context: Context, token: String) {
        mDatabase.child("Orders")
            .child(SimpleDateFormat("yyyyMMddHHmmssmsms").format(Date()) + Random().nextInt(1000000))
            .setValue(hash)
            .addOnSuccessListener {

                //send Notification
                sendNotification("Order", "Order Received", token, context)
                //clear the card
                myCart!!.removeValue()

                mLog("Order created")
            }.addOnFailureListener {

                mToast(context, "Failed to create order")
                mLog("FAILED TO CRAETE ORDER")
            }
    }

    fun getSellerToken(sellerID: String): String? {
        return mDatabase.child("Easybutor Distributor").child(sellerID).child("token").root.key
    }

    var myOrder = mDatabase.child("Orders")

    val urlOne = mDatabase.child("url1")
    val urlTwo = mDatabase.child("url2")
    val urlThree = mDatabase.child("url3")
    val urlFour = mDatabase.child("url4")

    fun addToWishList(context: Context, productID: String) {
        val orderDetails: HashMap<String, String> = HashMap<String, String>()
        mWishList?.orderByChild("id")?.equalTo(productID)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val timestamp =
                            SimpleDateFormat("yyyyMMddHHmmssmsms")
                                .format(Date()) + Random().nextInt(1000000)
                        orderDetails["id"] = productID
                        orderDetails["quantity"] = "1"

                        mWishList?.child(timestamp)?.setValue(orderDetails)
                            ?.addOnSuccessListener {
                                mToast(context, "Added to wishlist")
                            }
                    } else {
                        mToast(context, "Already added to wishlist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(context, error.message)
                }

            })
    }

    fun addToCard(context: Context, productID: String) {
        val orderDetails: HashMap<String, String> = HashMap<String, String>()
        myCart?.orderByChild("id")?.equalTo(productID)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val timestamp = SimpleDateFormat("yyyyMMddHHmmssmsms")
                            .format(Date()) + Random().nextInt(1000000)
                        orderDetails["id"] = productID
                        orderDetails["quantity"] = "1"

                        myCart?.child(timestamp)?.setValue(orderDetails)
                            ?.addOnSuccessListener {
                                mToast(context, "Added to cart")
                            }
                    } else {
                        mToast(context, "Already added to cart")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mToast(context, error.message)
                }

            })

    }
}
