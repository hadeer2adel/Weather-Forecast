package com.example.weatherforecast.LocalDataSource

import android.content.Context
import android.util.Log
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl constructor(
    val daoLastWeather: DAOLastWeather,
    val daoLocations: DAOLocations,
    val daoNotifications: DAONotifications
):LocalDataSource {

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

    override fun getAllNotifications(): Flow<List<NotificationData>> {
        return daoNotifications.getAllNotifications()
    }

    override suspend fun getNotificationById(date: String, time: String): NotificationData? {
        return daoNotifications.getNotificationById(date, time)
    }

    override suspend fun insertNotification(notification: NotificationData) {
        daoNotifications.insertNotification(notification)
    }

    override suspend fun deleteNotification(notification: NotificationData) {
        daoNotifications.deleteNotification(notification)
    }

    override suspend fun deleteNotificationById(date: String, time: String){
        daoNotifications.deleteNotificationById(date, time)
    }

    override suspend fun deleteAllNotifications() {
        daoNotifications.deleteAllNotifications()
    }

}