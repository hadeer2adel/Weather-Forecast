package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.Helpers.convertTimestampToDate

@Entity(tableName = "Weather")
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val Date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Long,
    val humidity: Int,
    val pressure: Long,
    val wind: Double,
    val cloudiness: Int,
)

fun getWeatherData(weather: CurrentWeatherResponse): WeatherData{
    return WeatherData(
        0,
        weather.coordinates.latitude,
        weather.coordinates.longitude,
        convertTimestampToDate(weather.timestamp, weather.timezone),
        weather.weather.get(0).description,
        weather.weather.get(0).icon,
        weather.main.temperature.toLong(),
        weather.main.humidity,
        weather.main.pressure.toLong(),
        weather.wind.speed,
        weather.clouds.cloudiness
    )
}