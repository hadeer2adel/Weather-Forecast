package com.example.weatherforecast.Helpers

import android.content.Context

private val FILE_NAME = "Settings"

fun saveOnSharedPreferences(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString(key, value)
        apply()
    }
}

fun getFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
    val sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPref.getString(key, defaultValue) ?: defaultValue
}
