package com.example.weatherforecast.Model

import androidx.annotation.NonNull
import androidx.room.Entity


@Entity(tableName = "Alert", primaryKeys = ["date", "time"])
data class AlertData(
    @NonNull
    val date: String,
    @NonNull
    val time: String,
    val latitude: Double,
    val longitude: Double,
    val notificationType: String,
)