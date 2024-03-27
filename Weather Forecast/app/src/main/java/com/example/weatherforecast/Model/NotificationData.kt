package com.example.weatherforecast.Model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar


@Entity(tableName = "Notification",
        indices = [Index(value = ["date", "time"], unique = true)])
data class NotificationData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val requestId: String,
    val date: String,
    val time: String,
    val notificationType: String,
)