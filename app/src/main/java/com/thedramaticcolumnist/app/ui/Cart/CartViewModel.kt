package com.thedramaticcolumnist.app.ui.Cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Cart Fragment"
    }
    val text: LiveData<String> = _text
}