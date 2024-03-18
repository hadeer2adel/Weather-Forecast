package com.example.weatherforecast.LocalDataSource

import android.content.Context
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(val context: Context):LocalDataSource {

    private val dao: LocalDAO

    init {
        val dataBase:DataBase = DataBase.getInstance(context)
        dao = dataBase.getDAO()
    }

    companion object {
        @Volatile
        private var instance: LocalDataSourceImpl? = null
        fun getInstance(context: Context): LocalDataSourceImpl {
            if(instance == null)
                instance = LocalDataSourceImpl(context)
            return instance as LocalDataSourceImpl
        }
    }

    override fun getAllLocations(): Flow<List<WeatherData>> {
        return dao.getAllLocations()
    }

    override suspend fun getLastWeather(): WeatherData {
        return dao.getLastWeather()
    }

    override suspend fun insertLocation(location: WeatherData) {
        return dao.insertLocation(location)
    }

    override suspend fun deleteLocation(location: WeatherData) {
        return dao.deleteLocation(location)
    }
}