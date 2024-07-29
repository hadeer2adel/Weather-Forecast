package com.example.weatherforecast.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.Model.AppSettings
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    private fun setLocale(context: Context) {
        val languageCode = AppSettings.getInstance(this).language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun attachBaseContext(newBase: Context?) {
        setLocale(newBase ?: this)
        super.attachBaseContext(newBase)
    }
}