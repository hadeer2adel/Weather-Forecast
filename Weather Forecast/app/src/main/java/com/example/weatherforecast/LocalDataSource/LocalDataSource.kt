package com.example.weatherforecast.LocalDataSource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
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

}