package com.thedramaticcolumnist.app.ui.Products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Products Fragment"
    }
    val text: LiveData<String> = _text
}