package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Location")
data class LocationData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val countryCode: String,
)

fun getLocationData(weather: CurrentWeatherResponse): LocationData{
    return LocationData(
        0,
        weather.coordinates.latitude,
        weather.coordinates.longitude,
        weather.cityName,
        weather.country.code
    )
}