package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.Model.getDailyWeatherData
import com.example.weatherforecast.Model.getHourlyWeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RemoteViewModel(private var repository: Repository) : ViewModel (){

    private var _weather = MutableLiveData<WeatherData>()
    var weather: LiveData<WeatherData> = _weather

    private var _hourlyWeatherList = MutableLiveData<List<HourlyWeatherData>>()
    var hourlyWeatherList: LiveData<List<HourlyWeatherData>> = _hourlyWeatherList

    private var _dayList = MutableLiveData<List<DailyWeatherData>>()
    var dayList: LiveData<List<DailyWeatherData>> = _dayList

    fun getCurrentWeather(latitude: Double, longitude: Double, language: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getCurrentWeather(latitude, longitude, language)
            if (response.isSuccessful && response.body() != null) {
                val data = getWeatherData(response.body()!!, true)
                _weather.postValue(data)
            }
        }
    }

    fun getHourlyWeather(latitude: Double, longitude: Double, language: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getForecastWeather(latitude, longitude, language)
            if (response.isSuccessful && response.body() != null) {
                val list = getHourlyWeatherData(response.body()!!)
                _hourlyWeatherList.postValue(list)
            }
        }
    }

    fun getDailyWeather(latitude: Double, longitude: Double, language: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getForecastWeather(latitude, longitude, language)
            if (response.isSuccessful && response.body() != null) {
                val list = getDailyWeatherData(response.body()!!)
                _dayList.postValue(list)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}