package com.example.weatherforecast.Helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationReceiver: BroadcastReceiver() {
    lateinit var repository: Repository
    lateinit var notificationPermission: NotificationPermission
    private var weather = MutableStateFlow<ApiCurrentWeatherResponse>(ApiCurrentWeatherResponse.Loading)
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null)
            return

        notificationPermission = NotificationPermission(context)

        val latitude = intent.getStringExtra("latitude")!!.toDouble()
        val longitude = intent.getStringExtra("longitude")!!.toDouble()
        val appSettings = AppSettings.getInstance(context)
        val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)

        repository = RepositoryImpl(RemoteDataSourceImpl.getInstance(), LocalDataSourceImpl.getInstance(context))
        getCurrentWeather(latitude, longitude, units, appSettings.language)
        handleCurrentWeatherResponse(appSettings.language, appSettings.temperatureUnit)
    }

    private fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getCurrentWeather(latitude, longitude, units, language)
                .catch {
                    weather.value = ApiCurrentWeatherResponse.Failure(it)
                }.collect {
                    weather.value = ApiCurrentWeatherResponse.Success(it!!)
                }
        }
    }
    private fun handleCurrentWeatherResponse(language: String, temperatureUnit: String){
        CoroutineScope(Dispatchers.Main).launch {
            weather.collectLatest { response ->
                when(response){
                    is ApiCurrentWeatherResponse.Loading -> { }
                    is ApiCurrentWeatherResponse.Success ->{
                        val weatherData = getWeatherData(response.data)

                        var weatherDescription = "It's ${weatherData.weatherDescription} now in ${weatherData.cityName}. The temperature forecast for today is ${weatherData.temperature} º$temperatureUnit."
                        if(language.equals("ar"))
                            weatherDescription = "الجو ${weatherData.weatherDescription} الآن في ${weatherData.cityName}. توقعات درجة الحرارة لليوم هي ${weatherData.temperature} º$temperatureUnit."

                        notificationPermission.sendNotification(weatherDescription)
                    }
                    is ApiCurrentWeatherResponse.Failure ->{ }
                }
            }
        }
    }

}