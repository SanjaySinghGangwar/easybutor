package com.thedramaticcolumnist.app.Utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

object mUtils {
    fun mToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun mLog(message: String) {
        Log.i("SANJAY", "mLog: $message")
    }

    fun isValidText(text: String?, editText: EditText): Boolean {
        if (TextUtils.isEmpty(text)) {
            editText.requestFocus()
            editText.error = "Mandatory"
            return false
        }
        return true
    }

    fun showLoader(progressBar: ProgressBar) {
        if (progressBar.visibility == View.GONE) {
            progressBar.visibility = View.VISIBLE
        }
    }

    fun hideLoader(progressBar: ProgressBar) {
        if (progressBar.visibility == View.VISIBLE) {
            progressBar.visibility = View.GONE
        }
    }
}