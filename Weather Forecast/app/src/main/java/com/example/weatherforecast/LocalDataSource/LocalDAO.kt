package com.example.weatherforecast.LocalDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Language

@Dao
interface LocalDAO {
    @Query("SELECT * FROM Weather WHERE currentLocation = 0")
    fun getAllLocations(): Flow<List<WeatherData>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: WeatherData)
    @Delete
    suspend fun deleteLocation(location: WeatherData)

    @Query("SELECT * FROM Weather WHERE currentLocation = 1")
    suspend fun getLastWeather(): WeatherData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastWeather(weather: WeatherData)
}