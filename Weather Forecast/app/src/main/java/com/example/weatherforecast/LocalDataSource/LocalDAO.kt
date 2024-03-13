package com.example.weatherforecast.LocalDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.WeatherData

@Dao
interface LocalDAO {
    @Query("SELECT * FROM Weather WHERE currentLocation = 0")
    suspend fun getAllLocations(): List<WeatherData>

    @Query("SELECT * FROM Weather WHERE currentLocation = 1")
    suspend fun getLastWeather(): WeatherData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: WeatherData)

    @Delete
    suspend fun deleteLocation(location: WeatherData)
}