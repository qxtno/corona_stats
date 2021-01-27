package com.github.qxtno.coronastats.helpers

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {
    fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}