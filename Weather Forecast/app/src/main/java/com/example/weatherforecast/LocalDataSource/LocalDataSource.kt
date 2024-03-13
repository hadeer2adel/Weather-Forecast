package com.example.weatherforecast.LocalDataSource

import com.example.weatherforecast.Model.WeatherData

interface LocalDataSource {
    suspend fun getAllLocations(): List<WeatherData>
    suspend fun getLastWeather(): WeatherData
    suspend fun insertLocation(location: WeatherData)
    suspend fun deleteLocation(location: WeatherData)
}