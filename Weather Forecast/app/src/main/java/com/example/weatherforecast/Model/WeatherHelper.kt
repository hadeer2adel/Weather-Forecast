package com.example.weatherforecast.Model

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("lon") val longitude: Double,
    @SerializedName("lat") val latitude: Double
)

data class Main(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double,
    val pressure: Double,
    val humidity: Int,
)

data class Wind(
    val speed: Double
)

data class Clouds(
    @SerializedName("all") val cloudiness: Int
)

data class Sys(
    @SerializedName("country") val code: String
)
