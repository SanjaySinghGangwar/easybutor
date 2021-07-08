package com.thedramaticcolumnist.app.ui.Distributor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistributerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Your Distributor Fragment"
    }
    val text: LiveData<String> = _text
}