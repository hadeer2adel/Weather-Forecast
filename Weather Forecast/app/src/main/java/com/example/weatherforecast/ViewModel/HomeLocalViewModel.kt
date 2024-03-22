package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeLocalViewModel(val repository: Repository) : ViewModel (){

    private var _weather = MutableLiveData<WeatherData>()
    var weather: LiveData<WeatherData> = _weather

    private var _hourList = MutableLiveData<List<HourlyWeatherData>>()
    var hourList: LiveData<List<HourlyWeatherData>> = _hourList

    private var _dayList = MutableLiveData<List<DailyWeatherData>>()
    var dayList: LiveData<List<DailyWeatherData>> = _dayList

    init {
        getLastWeather()
    }

    fun insertLastWeather(weather: WeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLastWeather(weather)
        }
    }
    fun insertLastWeatherHour(hourlyWeatherData: HourlyWeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLastWeatherHour(hourlyWeatherData)
        }
    }
    fun insertLastWeatherDay(dailyWeatherData: DailyWeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLastWeatherDay(dailyWeatherData)
        }
    }

    fun deleteLastWeather(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteLastWeather()
        }
    }

    fun getLastWeather(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeather()
                .collect {
                    _weather.postValue(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherHours()
                .collect {
                    _hourList.postValue(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherDays()
                .collect {
                    _dayList.postValue(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}