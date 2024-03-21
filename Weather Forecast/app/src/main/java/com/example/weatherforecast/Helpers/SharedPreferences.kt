package com.example.weatherforecast.Helpers

import android.content.Context

private val FILE_NAME = "Settings"

fun saveOnSharedPreferences(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString(key, value)
        apply()
    }
}

fun getFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
    val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
}

fun isSharedPreferencesContains(context: Context, key: String): Boolean {
    val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.contains(key)
}