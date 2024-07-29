package com.example.weatherforecast.Services.Caching

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.LocationData
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOLocations {
    @Query("SELECT * FROM Location")
    fun getAllLocations(): Flow<List<LocationData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: LocationData)

    @Delete
    suspend fun deleteLocation(location: LocationData)

    @Query("DELETE FROM Location")
    suspend fun deleteAllLocations()
}