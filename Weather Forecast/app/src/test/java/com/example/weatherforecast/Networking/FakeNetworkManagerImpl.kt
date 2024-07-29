package com.example.weatherforecast.Networking

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Services.Networking.NetworkManager
import retrofit2.Response

class FakeNetworkManagerImpl : NetworkManager {
    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Response<CurrentWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Response<ForecastWeatherResponse> {
        TODO("Not yet implemented")
    }
}