package com.example.weatherforecast.Repository

import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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

    override fun getLastWeather(): Flow<WeatherData> {
        return localDataSource.getLastWeather()
    }

    override fun getLastWeatherHours(): Flow<List<HourlyWeatherData>> {
        return localDataSource.getLastWeatherHours()
    }

    override fun getLastWeatherDays(): Flow<List<DailyWeatherData>> {
        return localDataSource.getLastWeatherDays()
    }

    override suspend fun insertLastWeather(weather: WeatherData) {
        localDataSource.insertLastWeather(weather)
    }

    override suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData) {
        localDataSource.insertLastWeatherHour(hourlyWeatherData)
    }

    override suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData) {
        localDataSource.insertLastWeatherDay(dailyWeatherData)
    }

    override suspend fun deleteLastWeather() {
        localDataSource.deleteLastWeather()
    }

    override fun getAllAlerts(): Flow<List<AlertData>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun getAlertById(date: String, time: String): AlertData? {
        return localDataSource.getAlertById(date, time)
    }

    override suspend fun insertAlert(alert: AlertData) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertData) {
        localDataSource.deleteAlert(alert)
    }

    override suspend fun deleteAlertById(date: String, time: String) {
        localDataSource.deleteAlertById(date, time)
    }

    override suspend fun deleteAllAlerts() {
        localDataSource.deleteAllAlerts()
    }
}