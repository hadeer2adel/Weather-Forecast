package com.example.weatherforecast.Application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.example.weatherforecast.Model.AppSettings
import java.util.Locale

class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val language = AppSettings.getInstance(context).language
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale(this)
    }

    private fun setLocale(context: Context) {
        val languageCode = AppSettings.getInstance(this).language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}