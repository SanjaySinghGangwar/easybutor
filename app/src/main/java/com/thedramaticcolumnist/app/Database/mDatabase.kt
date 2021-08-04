package com.thedramaticcolumnist.app.Database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


object mDatabase {
    /*var chatMessageDataBase = FirebaseStorage.getInstance().reference.child("chatData")
    var UserDatabase = FirebaseDatabase.getInstance().reference.child("USERs")*/


    var myCart = FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("Easybutor")
            .child(it).child("cart")
    }
    var myOrder = FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("Easybutor")
            .child(it).child("order")
    }
    var myProfile = FirebaseAuth.getInstance().currentUser?.uid?.let {
        FirebaseDatabase.getInstance().reference.child("Easybutor")
            .child(it)
    }

    var productDatabase = FirebaseDatabase.getInstance().reference.child("Products")

    var mAddress = myProfile?.child("address")
}
