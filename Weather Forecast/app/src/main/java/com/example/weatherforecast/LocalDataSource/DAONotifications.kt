package com.example.weatherforecast.LocalDataSource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.NotificationData
import kotlinx.coroutines.flow.Flow

@Dao
interface DAONotifications {
    @Query("SELECT * FROM Notification")
    fun getAllNotifications(): Flow<List<NotificationData>>

    @Query("SELECT * FROM Notification WHERE date = :date AND time = :time")
    suspend fun getNotificationById(date: String, time: String): NotificationData?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationData)

    @Delete
    suspend fun deleteNotification(notification: NotificationData)

    @Query("DELETE FROM Notification WHERE date = :date AND time = :time")
    suspend fun deleteNotificationById(date: String, time: String)

    @Query("DELETE FROM Notification")
    suspend fun deleteAllNotifications()
}