package com.thedramaticcolumnist.app.ui.CustomerService

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Customer Service Fragment"
    }
    val text: LiveData<String> = _text
}