package com.example.weatherforecast.NotificationUtil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.LocalDataSource.DataBase
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.NotificationViewModel
import com.example.weatherforecast.ViewModel.NotificationViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationReceiver: BroadcastReceiver() {
    lateinit var repository: Repository
    lateinit var notificationMaker: NotificationMaker
    private var weather = MutableStateFlow<ApiCurrentWeatherResponse>(ApiCurrentWeatherResponse.Loading)

    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null || (!AppSettings.getInstance(context).notification)) {
            return
        }
        notificationMaker = NotificationMaker(context)
        val notificationType = intent.getStringExtra("notificationType")!!
        val date = intent.getStringExtra("date")!!
        val time = intent.getStringExtra("time")!!

        val latitude = intent.getStringExtra("latitude")!!.toDouble()
        val longitude = intent.getStringExtra("longitude")!!.toDouble()
        val appSettings = AppSettings.getInstance(context)
        val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)

        val dataBase: DataBase = DataBase.getInstance(context)
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAONotifications())
        repository = RepositoryImpl(RemoteDataSourceImpl.getInstance(), localDataSource)

        getCurrentWeather(latitude, longitude, units, appSettings.language)
        handleCurrentWeatherResponse(notificationType, appSettings.language, appSettings.temperatureUnit)
        deleteNotification(date, time)
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
    private fun handleCurrentWeatherResponse(notificationType: String, language: String, temperatureUnit: String){
        CoroutineScope(Dispatchers.Main).launch {
            weather.collectLatest { response ->
                when(response){
                    is ApiCurrentWeatherResponse.Loading -> { }
                    is ApiCurrentWeatherResponse.Success ->{
                        val weatherData = getWeatherData(response.data)

                        var weatherDescription = "It's ${weatherData.weatherDescription} now in ${weatherData.cityName}. The temperature forecast for today is ${weatherData.temperature} º$temperatureUnit."
                        if(language.equals("ar"))
                            weatherDescription = "الجو ${weatherData.weatherDescription} الآن في ${weatherData.cityName}. توقعات درجة الحرارة لليوم هي ${weatherData.temperature} º$temperatureUnit."

                        if (notificationType.equals(Context.NOTIFICATION_SERVICE))
                            notificationMaker.makeNotification(weatherDescription)
                        else
                            notificationMaker.makeAlarm(weatherDescription)
                    }
                    is ApiCurrentWeatherResponse.Failure ->{ }
                }
            }
        }
    }
    private fun deleteNotification(data: String, time: String){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNotificationById(data, time)
        }
    }
}