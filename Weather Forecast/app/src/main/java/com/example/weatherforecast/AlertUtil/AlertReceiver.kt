package com.example.weatherforecast.AlertUtil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.weatherforecast.Helpers.City
import com.example.weatherforecast.Helpers.getCity
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.Helpers.isNetworkConnected
import com.example.weatherforecast.LocalDataSource.DataBase
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertReceiver: BroadcastReceiver() {
    lateinit var repository: Repository
    lateinit var alertMaker: AlertMaker
    private var weather =
        MutableStateFlow<ApiCurrentWeatherResponse>(ApiCurrentWeatherResponse.Loading)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null || (!AppSettings.getInstance(context).notification)) {
            return
        }

        alertMaker = AlertMaker(context)
        val dataBase: DataBase = DataBase.getInstance(context)
        val localDataSource = LocalDataSourceImpl(
            dataBase.getDAOLastWeather(),
            dataBase.getDAOLocations(),
            dataBase.getDAOAlerts()
        )
        repository = RepositoryImpl(RemoteDataSourceImpl.getInstance(), localDataSource)

        if (isNetworkConnected(context)) {
            showWeatherAlert(context, intent)
        } else {
            alertMaker.makeNotification(context.getString(R.string.no_connection_notification))
        }

        val date = intent.getStringExtra("date")!!
        val time = intent.getStringExtra("time")!!
        deleteAlert(date, time)
    }

    private fun showWeatherAlert(context: Context, intent: Intent) {
        val notificationType = intent.getStringExtra("notificationType")!!

        val latitude = intent.getStringExtra("latitude")!!.toDouble()
        val longitude = intent.getStringExtra("longitude")!!.toDouble()
        val appSettings = AppSettings.getInstance(context)
        val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)
        val cityName = getCity(context, latitude, longitude).cityName

        getCurrentWeather(latitude, longitude, units, appSettings.language)
        handleCurrentWeatherResponse(
            notificationType,
            appSettings.language,
            appSettings.temperatureUnit,
            cityName
        )
    }

    private fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getCurrentWeather(latitude, longitude, units, language)
                .catch {
                    weather.value = ApiCurrentWeatherResponse.Failure(it)
                }.collect {
                    weather.value = ApiCurrentWeatherResponse.Success(it!!)
                }
        }
    }

    private fun handleCurrentWeatherResponse(
        notificationType: String,
        language: String,
        temperatureUnit: String,
        cityName: String
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            weather.collectLatest { response ->
                when (response) {
                    is ApiCurrentWeatherResponse.Loading -> {}
                    is ApiCurrentWeatherResponse.Success -> {
                        val weatherData = getWeatherData(response.data)

                        var weatherDescription = "It's ${weatherData.weatherDescription} now in ${cityName}. The temperature forecast for today is ${weatherData.temperature} º$temperatureUnit."
                        if (language.equals("ar"))
                            weatherDescription = "الجو ${weatherData.weatherDescription} الآن في ${cityName}. توقعات درجة الحرارة لليوم هي ${weatherData.temperature} º$temperatureUnit."

                        if (notificationType.equals(Context.NOTIFICATION_SERVICE))
                            alertMaker.makeNotification(weatherDescription)
                        else
                            alertMaker.makeAlarm(weatherDescription)
                    }
                    is ApiCurrentWeatherResponse.Failure -> {}
                }
            }
        }
    }

    private fun deleteAlert(data: String, time: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAlertById(data, time)
        }
    }
}