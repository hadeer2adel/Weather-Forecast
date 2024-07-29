package com.example.weatherforecast.Services.Caching

import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl constructor(
    val daoLastWeather: DAOLastWeather,
    val daoLocations: DAOLocations,
    val daoAlerts: DAOAlerts
): LocalDataSource {

    override fun getAllLocations(): Flow<List<LocationData>> {
        return daoLocations.getAllLocations()
    }

    override suspend fun insertLocation(location: LocationData) {
        daoLocations.insertLocation(location)
    }

    override suspend fun deleteLocation(location: LocationData) {
        daoLocations.deleteLocation(location)
    }

    override suspend fun deleteAllLocations() {
        daoLocations.deleteAllLocations()
    }

    override fun getLastWeather(): Flow<WeatherData> {
        return daoLastWeather.getLastWeather()
    }

    override fun getLastWeatherHours(): Flow<List<HourlyWeatherData>> {
        return daoLastWeather.getLastWeatherHours()
    }

    override fun getLastWeatherDays(): Flow<List<DailyWeatherData>> {
        return daoLastWeather.getLastWeatherDays()
    }

    override suspend fun insertLastWeather(weather: WeatherData) {
        daoLastWeather.insertLastWeather(weather)
    }

    override suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData) {
        daoLastWeather.insertLastWeatherHour(hourlyWeatherData)
    }

    override suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData) {
        daoLastWeather.insertLastWeatherDay(dailyWeatherData)
    }

    override suspend fun deleteLastWeather() {
        daoLastWeather.deleteLastWeather()
        daoLastWeather.deleteLastWeatherHours()
        daoLastWeather.deleteLastWeatherDays()
    }

    override fun getAllAlerts(): Flow<List<AlertData>> {
        return daoAlerts.getAllAlerts()
    }

    override suspend fun getAlertById(date: String, time: String): AlertData? {
        return daoAlerts.getAlertById(date, time)
    }

    override suspend fun insertAlert(alert: AlertData) {
        daoAlerts.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertData) {
        daoAlerts.deleteAlert(alert)
    }

    override suspend fun deleteAlertById(date: String, time: String){
        daoAlerts.deleteAlertById(date, time)
    }

    override suspend fun deleteAllAlerts() {
        daoAlerts.deleteAllAlerts()
    }

}