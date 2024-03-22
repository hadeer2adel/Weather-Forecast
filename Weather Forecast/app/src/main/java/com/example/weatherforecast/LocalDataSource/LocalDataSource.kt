package com.example.weatherforecast.LocalDataSource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    //location
    fun getAllLocations(): Flow<List<LocationData>>
    suspend fun insertLocation(location: LocationData)
    suspend fun deleteLocation(location: LocationData)
    suspend fun deleteAllLocations()
}