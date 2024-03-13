package com.example.weatherforecast.Helpers

fun getWeatherIconUrl(icon: String):String{
    return "http://openweathermap.org/img/w/$icon.png"
}

fun getWeatherImageUrl(icon: String):String{
    return "http://openweathermap.org/img/wn/$icon@4x.png"
}

fun getCountryFlagUrl(countryCode: String):String {
    return "https://flagsapi.com/$countryCode/shiny/64.png"
}