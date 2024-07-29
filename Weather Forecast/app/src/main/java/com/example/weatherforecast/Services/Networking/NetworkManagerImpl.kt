package com.example.weatherforecast.Services.Networking

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManagerImpl : NetworkManager {

    private val API_URL = "https://api.openweathermap.org/data/2.5/"
    private var apiKey = "7c25a82b0bf376312b96832ded4861a8"
    private val service: NetworkService

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
        service = retrofit.create(NetworkService:: class.java)
    }

    companion object {
        @Volatile
        private var instance: NetworkManagerImpl? = null
        fun getInstance(): NetworkManagerImpl {
            if(instance == null)
                instance = NetworkManagerImpl()
            return instance as NetworkManagerImpl
        }
    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Response<CurrentWeatherResponse> {
        return service.getCurrentWeather(latitude, longitude, units, language, apiKey)
    }

    override suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Response<ForecastWeatherResponse> {
        return service.getForecastWeather(latitude, longitude, units, language, 40, apiKey)
    }
}