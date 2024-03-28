package com.example.weatherforecast.Repository

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {
    //Remote
    suspend fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String): Flow<CurrentWeatherResponse?>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String): Flow<ForecastWeatherResponse?>
    
    //Local
    fun getAllLocations(): Flow<List<LocationData>>
    suspend fun insertLocation(location: LocationData)
    suspend fun deleteLocation(location: LocationData)
    suspend fun deleteAllLocations()

    fun getLastWeather(): Flow<WeatherData>
    fun getLastWeatherHours(): Flow<List<HourlyWeatherData>>
    fun getLastWeatherDays(): Flow<List<DailyWeatherData>>
    suspend fun insertLastWeather(weather: WeatherData)
    suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData)
    suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData)
    suspend fun deleteLastWeather()

    fun getAllNotifications(): Flow<List<NotificationData>>
    suspend fun getNotificationById(date: String, time: String): NotificationData?
    suspend fun insertNotification(notification: NotificationData)
    suspend fun deleteNotification(notification: NotificationData)
    suspend fun deleteNotificationById(date: String, time: String)
    suspend fun deleteAllNotifications()
}