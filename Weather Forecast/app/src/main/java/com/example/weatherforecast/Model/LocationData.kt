package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Location",
    indices = [Index(value = ["latitude", "longitude"], unique = true)])
data class LocationData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double
)

fun getLocationData(weather: CurrentWeatherResponse): LocationData{
    return LocationData(
        0,
        weather.coordinates.latitude,
        weather.coordinates.longitude
    )
}