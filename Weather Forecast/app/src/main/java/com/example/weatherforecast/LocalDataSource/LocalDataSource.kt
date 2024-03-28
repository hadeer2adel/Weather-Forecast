package com.example.weatherforecast.LocalDataSource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    //location
    fun getAllLocations(): Flow<List<LocationData>>
    suspend fun insertLocation(location: LocationData)
    suspend fun deleteLocation(location: LocationData)
    suspend fun deleteAllLocations()

    //last weather
    fun getLastWeather(): Flow<WeatherData>
    fun getLastWeatherHours(): Flow<List<HourlyWeatherData>>
    fun getLastWeatherDays(): Flow<List<DailyWeatherData>>

    suspend fun insertLastWeather(weather: WeatherData)
    suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData)
    suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData)

    suspend fun deleteLastWeather()

    //Notification
    fun getAllNotifications(): Flow<List<NotificationData>>
    suspend fun getNotificationById(date: String, time: String): NotificationData?
    suspend fun insertNotification(notification: NotificationData)
    suspend fun deleteNotification(notification: NotificationData)
    suspend fun deleteNotificationById(date: String, time: String)
    suspend fun deleteAllNotifications()

}