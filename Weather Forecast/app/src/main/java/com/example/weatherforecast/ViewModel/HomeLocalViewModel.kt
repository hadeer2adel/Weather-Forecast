package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

    fun insertLastWeather(weather: WeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLastWeather(weather)
        }
    }
    fun insertLastWeatherHour(hourlyWeatherData: List<HourlyWeatherData>){
        viewModelScope.launch(Dispatchers.IO){
            for(weather in hourlyWeatherData)
                repository.insertLastWeatherHour(weather)
        }
    }
    fun insertLastWeatherDay(dailyWeatherData: List<DailyWeatherData>){
        viewModelScope.launch(Dispatchers.IO){
            for(weather in dailyWeatherData)
                repository.insertLastWeatherDay(weather)
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

class HomeLocalViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeLocalViewModel::class.java)){
            HomeLocalViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}