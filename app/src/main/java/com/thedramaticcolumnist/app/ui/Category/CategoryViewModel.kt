package com.thedramaticcolumnist.app.ui.Category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Category Fragment"
    }
    val text: LiveData<String> = _text
}