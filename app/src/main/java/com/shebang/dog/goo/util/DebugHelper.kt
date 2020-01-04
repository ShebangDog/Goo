package com.shebang.dog.goo.util

import android.content.Context
import android.util.Log
import android.widget.Toast

object DebugHelper {
    private const val TAG = "ShebangApp"

    fun log(message: String) {
        Log.d(TAG, message)
    }

    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        log(message)
    }
}