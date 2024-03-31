package com.example.weatherforecast.LocalDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.AlertData
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOAlerts {
    @Query("SELECT * FROM Alert")
    fun getAllAlerts(): Flow<List<AlertData>>

    @Query("SELECT * FROM Alert WHERE date = :date AND time = :time")
    suspend fun getAlertById(date: String, time: String): AlertData?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alert: AlertData)

    @Delete
    suspend fun deleteAlert(alert: AlertData)

    @Query("DELETE FROM Alert WHERE date = :date AND time = :time")
    suspend fun deleteAlertById(date: String, time: String)

    @Query("DELETE FROM Alert")
    suspend fun deleteAllAlerts()
}