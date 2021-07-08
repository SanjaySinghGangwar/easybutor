package com.thedramaticcolumnist.app.Utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object mUtils {
    fun mToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun mLog(message: String) {
        Log.i("SANJAY", "mLog: $message")
    }
}