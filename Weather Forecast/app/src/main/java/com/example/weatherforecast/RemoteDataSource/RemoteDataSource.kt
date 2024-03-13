package com.example.weatherforecast.RemoteDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, language: String): Response<CurrentWeatherResponse>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, language: String): Response<ForecastWeatherResponse>
}