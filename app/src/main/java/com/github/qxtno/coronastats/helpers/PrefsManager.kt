package com.github.qxtno.coronastats.helpers

import android.content.Context
import android.content.SharedPreferences

class PrefsManager {
    companion object {
        private const val NAME = "SETTINGS"
    }

    fun setCountryString(context: Context, key: String, string: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, string).apply()
    }

    fun setFirstLaunch(context: Context, key: String, isFirst: Boolean) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, isFirst).apply()
    }

    fun getCountryString(context: Context, key: String, defaultValue: String): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getFirstLaunch(context: Context, key: String, defaultValue: Boolean): Boolean {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

}