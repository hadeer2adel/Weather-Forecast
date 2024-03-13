package com.example.weatherforecast.Repository

import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import retrofit2.Response

class RepositoryImpl (val remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource):
    Repository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, language: String
    ): Response<CurrentWeatherResponse> {
        return  remoteDataSource.getCurrentWeather(latitude, longitude, language)
    }


    override suspend fun getForecastWeather(latitude: Double, longitude: Double, language: String
    ): Response<ForecastWeatherResponse> {
        return  remoteDataSource.getForecastWeather(latitude, longitude, language)
    }

    override suspend fun getAllLocations(): List<WeatherData> {
        return localDataSource.getAllLocations()
    }

    override suspend fun getLastWeather(): WeatherData {
        return localDataSource.getLastWeather()
    }

    override suspend fun insertLocation(weather: WeatherData) {
        localDataSource.insertLocation(weather)
    }

    override suspend fun deleteLocation(weather: WeatherData) {
        localDataSource.deleteLocation(weather)
    }
}