package com.example.weatherforecast.Repository

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.WeatherData
import retrofit2.Response

interface Repository {
    //Remote
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, language: String): Response<CurrentWeatherResponse>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, language: String): Response<ForecastWeatherResponse>
    
    //Local
    suspend fun getAllLocations(): List<WeatherData>
    suspend fun getLastWeather(): WeatherData
    suspend fun insertLocation(weather: WeatherData)
    suspend fun deleteLocation(weather: WeatherData)
}