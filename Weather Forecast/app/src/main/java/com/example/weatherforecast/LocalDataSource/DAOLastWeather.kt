package com.example.weatherforecast.LocalDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOLastWeather {

    @Query("SELECT * FROM Weather")
    fun getLastWeather(): Flow<WeatherData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastWeather(weather: WeatherData)

    @Query("DELETE FROM Weather")
    suspend fun deleteLastWeather()




    @Query("SELECT * FROM Hours")
    fun getLastWeatherHours(): Flow<List<HourlyWeatherData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData)

    @Query("DELETE FROM Hours")
    suspend fun deleteLastWeatherHours()




    @Query("SELECT * FROM Days")
    fun getLastWeatherDays(): Flow<List<DailyWeatherData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData)

    @Query("DELETE FROM Days")
    suspend fun deleteLastWeatherDays()
}