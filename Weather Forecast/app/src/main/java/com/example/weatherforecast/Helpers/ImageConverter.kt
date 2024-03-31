package com.example.weatherforecast.Helpers

import com.example.weatherforecast.R

fun getWeatherIcon(icon: String): Int{
    return when (icon) {
        "01d" -> R.drawable.w01d
        "01n" -> R.drawable.w01n
        "02d" -> R.drawable.w02d
        "02n" -> R.drawable.w02n
        "03d", "03n" -> R.drawable.w03
        "04d", "04n" -> R.drawable.w04
        "09d", "09n", "10d", "10n"  -> R.drawable.w09
        "11d", "11n" -> R.drawable.w11
        "13d", "13n" -> R.drawable.w13
        "50d", "50n" -> R.drawable.w50
        else -> R.drawable.w03
    }
}

fun getCountryFlagUrl(countryCode: String):String {
    return "https://flagsapi.com/$countryCode/shiny/64.png"
}