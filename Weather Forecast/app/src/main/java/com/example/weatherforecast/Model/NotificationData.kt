package com.example.weatherforecast.Model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar


@Entity(tableName = "Notification", primaryKeys = ["date", "time"])
data class NotificationData(
    @NonNull
    val date: String,
    @NonNull
    val time: String,
    val latitude: Double,
    val longitude: Double,
    val notificationType: String,
)