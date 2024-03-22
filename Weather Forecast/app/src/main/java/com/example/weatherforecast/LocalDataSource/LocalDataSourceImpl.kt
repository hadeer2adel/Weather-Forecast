package com.example.weatherforecast.LocalDataSource

import android.content.Context
import android.util.Log
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
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

    override fun getLastWeather(): Flow<WeatherData> {
        return daoLastWeather.getLastWeather()
    }

    override fun getLastWeatherHours(): Flow<List<HourlyWeatherData>> {
        return daoLastWeather.getLastWeatherHours()
    }

    override fun getLastWeatherDays(): Flow<List<DailyWeatherData>> {
        return daoLastWeather.getLastWeatherDays()
    }

    override suspend fun insertLastWeather(weather: WeatherData) {
        daoLastWeather.insertLastWeather(weather)
    }

    override suspend fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData) {
        daoLastWeather.insertLastWeatherHour(hourlyWeatherData)
    }

    override suspend fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData) {
        daoLastWeather.insertLastWeatherDay(dailyWeatherData)
    }

    override suspend fun deleteLastWeather() {
        daoLastWeather.deleteLastWeather()
        daoLastWeather.deleteLastWeatherHours()
        daoLastWeather.deleteLastWeatherDays()
    }

}