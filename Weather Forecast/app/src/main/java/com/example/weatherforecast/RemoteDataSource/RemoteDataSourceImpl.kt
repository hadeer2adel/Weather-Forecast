package com.example.weatherforecast.RemoteDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceImpl : RemoteDataSource {

    private val API_URL = "https://api.openweathermap.org/data/2.5/"
    private var apiKey = "7c25a82b0bf376312b96832ded4861a8"
    private val service: RemoteDAO

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
        service = retrofit.create(RemoteDAO:: class.java)
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSourceImpl? = null
        fun getInstance(): RemoteDataSourceImpl {
            if(instance == null)
                instance = RemoteDataSourceImpl()
            return instance as RemoteDataSourceImpl
        }
    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Response<CurrentWeatherResponse> {
        return service.getCurrentWeather(latitude, longitude, language, units, apiKey)
    }

    override suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Response<ForecastWeatherResponse> {
        return service.getForecastWeather(latitude, longitude, language, units, 40, apiKey)
    }
}