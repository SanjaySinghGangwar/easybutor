package com.thedramaticcolumnist.app.Utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import kotlin.math.roundToInt


object mUtils {
    fun mToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun mLog(message: String) {
        Log.i("SANJAY", "mLog: $message")
        print("SANJAY :: $message")
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

    fun calculateDiscount(price: String, mrp: String): String {
        return 100.minus((price.toFloat() / mrp.toFloat()) * 100).roundToInt()
            .toString() + " %"
    }
    fun hideKeyboard(mContext: Context) {
        val imm: InputMethodManager = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            (mContext as Activity).window
                .currentFocus!!.windowToken, 0
        )
    }
}