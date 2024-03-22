package com.example.weatherforecast.LocalDataSource

import android.content.Context
import android.util.Log
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl private constructor(val context: Context):LocalDataSource {

    private val daoLocations: DAOLocations
    private val daoLastWeather: DAOLastWeather

    init {
        val dataBase:DataBase = DataBase.getInstance(context)
        daoLocations = dataBase.getDAOLocations()
        daoLastWeather = dataBase.getDAOLastWeather()
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

    override fun getAllLocations(): Flow<List<LocationData>> {
        return daoLocations.getAllLocations()
    }

    override suspend fun insertLocation(location: LocationData) {
        daoLocations.insertLocation(location)
    }

    override suspend fun deleteLocation(location: LocationData) {
        daoLocations.deleteLocation(location)
    }

    override suspend fun deleteAllLocations() {
        daoLocations.deleteAllLocations()
    }

}