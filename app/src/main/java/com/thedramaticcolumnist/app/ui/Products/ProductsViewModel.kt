package com.thedramaticcolumnist.app.ui.Products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class ProductsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Products Fragment"
    }
    val text: LiveData<String> = _text

    private val _data = MutableLiveData<String>().apply {
        value = FirebaseDatabase.getInstance().reference.child("Products").toString()
    }
    val data: LiveData<String> = _data


}