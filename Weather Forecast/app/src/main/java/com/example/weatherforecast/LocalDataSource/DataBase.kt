package com.example.weatherforecast.LocalDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.Model.WeatherData

@Database(entities = arrayOf(WeatherData::class), version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun getDAO(): LocalDAO

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getInstance(ctx: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, DataBase::class.java, "weatherDB_1")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}