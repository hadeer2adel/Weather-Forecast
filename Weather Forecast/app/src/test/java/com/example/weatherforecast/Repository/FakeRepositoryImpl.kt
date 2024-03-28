package com.example.weatherforecast.Repository

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

class FakeRepositoryImpl: Repository {
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
        TODO("Not yet implemented")
    }

    override suspend fun insertLocation(location: LocationData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocation(location: LocationData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllLocations() {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationById(date: String, time: String): NotificationData? {
        TODO("Not yet implemented")
    }

    override suspend fun insertNotification(notification: NotificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotification(notification: NotificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNotificationById(date: String, time: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNotifications() {
        TODO("Not yet implemented")
    }
}