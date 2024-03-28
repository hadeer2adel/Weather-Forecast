package com.example.weatherforecast.RemoteDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response

class FakeRemoteDataSourceImpl : RemoteDataSource {
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