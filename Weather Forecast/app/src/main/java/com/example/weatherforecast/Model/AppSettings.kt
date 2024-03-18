package com.example.weatherforecast.Model

import android.content.Context
import com.example.weatherforecast.Helpers.getFromSharedPreferences
import com.example.weatherforecast.Helpers.saveOnSharedPreferences
import com.example.weatherforecast.LocalDataSource.DataBase
import com.example.weatherforecast.LocalDataSource.LocalDAO
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.R

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

    init {
        context = _context
        latitude = getFromSharedPreferences(context, "latitude", "0").toDouble()
        longitude = getFromSharedPreferences(context, "longitude", "0").toDouble()
        notification =  getFromSharedPreferences(context, "notification", "true").toBoolean()
        language =  getFromSharedPreferences(context, "language", "eg")
        temperatureUnit = getFromSharedPreferences(context, "temperatureUnit", "K")
        windUnit =  getFromSharedPreferences(context, "windUnit", "m/s")
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
}