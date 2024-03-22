package com.example.weatherforecast.Repository

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {
    //Remote
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String): Flow<CurrentWeatherResponse?>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String): Flow<ForecastWeatherResponse?>
    
    //Local
    fun getAllLocations(): Flow<List<LocationData>>
    suspend fun insertLocation(location: LocationData)
    suspend fun deleteLocation(location: LocationData)
    suspend fun deleteAllLocations()
}