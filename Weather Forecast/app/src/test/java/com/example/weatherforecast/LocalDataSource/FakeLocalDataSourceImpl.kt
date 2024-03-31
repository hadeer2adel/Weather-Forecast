package com.example.weatherforecast.LocalDataSource

import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalDataSourceImpl (
    private var alertList: MutableList<AlertData>? = mutableListOf()
): LocalDataSource {
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

    override fun getAllAlerts(): Flow<List<AlertData>> {
        return MutableStateFlow(alertList ?: emptyList())
    }

    override suspend fun getAlertById(date: String, time: String): AlertData? {
        if (alertList == null)
            return null

        for (alert in alertList!!){
            if (alert.date.equals(date) && alert.time.equals(time))
                return alert
        }
        return null
    }

    override suspend fun insertAlert(alert: AlertData) {
        alertList?.add(alert)
    }

    override suspend fun deleteAlert(alert: AlertData) {
        alertList?.remove(alert)
    }

    override suspend fun deleteAlertById(date: String, time: String) {
        if (alertList != null) {
            for (alert in alertList!!) {
                if (alert.date.equals(date) && alert.time.equals(time)) {
                    alertList?.remove(alert)
                    break
                }
            }
        }
    }

    override suspend fun deleteAllAlerts() {
        alertList?.clear()
    }
}