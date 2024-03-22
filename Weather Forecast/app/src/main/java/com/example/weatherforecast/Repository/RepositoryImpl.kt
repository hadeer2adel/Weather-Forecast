package com.example.weatherforecast.Repository

import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class RepositoryImpl (val remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource):
    Repository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Flow<CurrentWeatherResponse?> {
        return  flowOf(remoteDataSource.getCurrentWeather(latitude, longitude, units, language).body())
    }


    override suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String
    ): Flow<ForecastWeatherResponse?> {
        return  flowOf(remoteDataSource.getForecastWeather(latitude, longitude, units, language).body())
    }

    override fun getAllLocations(): Flow<List<LocationData>> {
        return localDataSource.getAllLocations()
    }

    override suspend fun insertLocation(weather: LocationData) {
        localDataSource.insertLocation(weather)
    }

    override suspend fun deleteLocation(weather: LocationData) {
        localDataSource.deleteLocation(weather)
    }

    override suspend fun deleteAllLocations() {
        localDataSource.deleteAllLocations()
    }
}