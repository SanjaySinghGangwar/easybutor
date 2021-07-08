package com.thedramaticcolumnist.app.ui.WishList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WishlistViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is WishList Fragment"
    }
    val text: LiveData<String> = _text
}