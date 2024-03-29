package com.example.weatherforecast.Repository

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRepositoryImpl(
    private var notificationList: MutableList<NotificationData>? = mutableListOf(),
    private var locationList: MutableList<LocationData>? = mutableListOf()

): Repository {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Flow<CurrentWeatherResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ): Flow<ForecastWeatherResponse?> {
        TODO("Not yet implemented")
    }

    override fun getAllLocations(): Flow<List<LocationData>> {
        return MutableStateFlow(locationList ?: emptyList())
    }

    override suspend fun insertLocation(location: LocationData) {
        locationList?.add(location)
    }

    override suspend fun deleteLocation(location: LocationData) {
        locationList?.remove(location)
    }
    override suspend fun deleteAllLocations() {
        locationList?.clear()
    }

    override fun getLastWeather(): Flow<WeatherData> {
        TODO("Not yet implemented")
    }

    override fun getLastWeatherHours(): Flow<List<HourlyWeatherData>> {
        TODO("Not yet implemented")
    }

    override fun getLastWeatherDays(): Flow<List<DailyWeatherData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLastWeather(weather: WeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLastWeather() {
        TODO("Not yet implemented")
    }

    override fun getAllNotifications(): Flow<List<NotificationData>> {
        return MutableStateFlow(notificationList ?: emptyList())
    }

    override suspend fun getNotificationById(date: String, time: String): NotificationData? {
        if (notificationList == null)
            return null

        for (notification in notificationList!!){
            if (notification.date.equals(date) && notification.time.equals(time))
                return notification
        }
        return null
    }

    override suspend fun insertNotification(notification: NotificationData) {
        notificationList?.add(notification)
    }

    override suspend fun deleteNotification(notification: NotificationData) {
        notificationList?.remove(notification)
    }

    override suspend fun deleteNotificationById(date: String, time: String) {
        if (notificationList != null) {
            for (notification in notificationList!!) {
                if (notification.date.equals(date) && notification.time.equals(time)) {
                    notificationList?.remove(notification)
                    break
                }
            }
        }
    }

    override suspend fun deleteAllNotifications() {
        notificationList?.clear()
    }
}