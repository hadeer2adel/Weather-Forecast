package com.example.weatherforecast.Services.Networking

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response

interface NetworkManager {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String): Response<CurrentWeatherResponse>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String): Response<ForecastWeatherResponse>
}