package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Helpers.getFromSharedPreferences
import com.example.weatherforecast.Helpers.isSharedPreferencesContains
import com.example.weatherforecast.Helpers.saveOnSharedPreferences

class AppSettings private constructor(_context: Context) {
    private val context: Context

    var latitude: Double = 0.0
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "latitude", value.toString())
        }
    var longitude: Double = 0.0
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "longitude", value.toString())
        }
    var notification: Boolean = true
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "notification", field.toString())
        }
    var language: String = "eg"
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "language", field)
        }
    var temperatureUnit: String = "K"
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "temperatureUnit", field)
        }
    var windUnit: String = "m/s"
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "windUnit", field)
        }
    var locationMethod: String = "gps"
        get() = field
        set(value) {
            field = value
            saveOnSharedPreferences(context, "locationMethod", field)
        }
    init {
        context = _context
        latitude = getFromSharedPreferences(context, "latitude", "0").toDouble()
        longitude = getFromSharedPreferences(context, "longitude", "0").toDouble()
        notification =  getFromSharedPreferences(context, "notification", "true").toBoolean()
        language =  getFromSharedPreferences(context, "language", "eg")
        temperatureUnit = getFromSharedPreferences(context, "temperatureUnit", "K")
        windUnit =  getFromSharedPreferences(context, "windUnit", "m/s")
        locationMethod =  getFromSharedPreferences(context, "locationMethod", "gps")
    }

    companion object {
        @Volatile
        private var instance: AppSettings? = null
        fun getInstance(context: Context): AppSettings {
            if (instance == null)
                instance = AppSettings(context)
            return instance as AppSettings
        }
    }

    fun isLatitudeSaved() = isSharedPreferencesContains(context, "latitude")
    fun isLongitudeSaved() = isSharedPreferencesContains(context, "longitude")
}