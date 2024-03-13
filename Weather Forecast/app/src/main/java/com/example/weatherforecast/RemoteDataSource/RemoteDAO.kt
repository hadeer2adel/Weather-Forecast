package com.example.weatherforecast.RemoteDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteDAO {

    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") latitude: Double,
                                  @Query("lon") longitude: Double,
                                  @Query("lang") language: String,
                                  @Query("appid") apiKey: String): Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun getForecastWeather(@Query("lat") latitude: Double,
                                   @Query("lon") longitude: Double,
                                   @Query("lang") language: String,
                                   @Query("cnt") cnt: Int,
                                   @Query("appid") apiKey: String): Response<ForecastWeatherResponse>

}