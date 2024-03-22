package com.example.weatherforecast.LocalDataSource

import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllLocations(): Flow<List<WeatherData>>
    suspend fun insertLocation(location: WeatherData)
    suspend fun deleteLocation(location: WeatherData)
    suspend fun getLastWeather(): WeatherData
    suspend fun insertLastWeather(weather: WeatherData)
}