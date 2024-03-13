package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("coord") val coordinates: Coordinates,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val timezone: Long,
    @SerializedName("sys") val country: Sys,
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("name") val cityName: String,
)

data class Weather(
    val description: String,
    val icon: String
)

data class ForecastWeatherResponse(
    val list: List<ForecastWeather>
)

data class ForecastWeather(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("dt_txt") val dateTimeText: String,
    val main: Main,
    val weather: List<Weather>
)