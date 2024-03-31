package com.example.weatherforecast.LocalDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData

@Database(entities = arrayOf(LocationData::class, WeatherData::class, HourlyWeatherData::class, DailyWeatherData::class, AlertData::class), version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun getDAOLastWeather(): DAOLastWeather
    abstract fun getDAOLocations(): DAOLocations
    abstract fun getDAOAlerts(): DAOAlerts

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getInstance(ctx: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, DataBase::class.java, "weatherDB_12")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}