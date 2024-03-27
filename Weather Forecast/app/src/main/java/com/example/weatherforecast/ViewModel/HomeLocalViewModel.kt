package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.LocalDataSource.DaoDailyWeatherDataResponse
import com.example.weatherforecast.LocalDataSource.DaoHourlyWeatherResponse
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DaoWeatherResponse
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class HomeLocalViewModel(val repository: Repository) : ViewModel (){

    private var _weather = MutableStateFlow<DaoWeatherResponse>(DaoWeatherResponse.Loading)
    var weather: StateFlow<DaoWeatherResponse> = _weather

    private var _hourList = MutableStateFlow<DaoHourlyWeatherResponse>(DaoHourlyWeatherResponse.Loading)
    var hourList: StateFlow<DaoHourlyWeatherResponse> = _hourList

    private var _dayList = MutableStateFlow<DaoDailyWeatherDataResponse>(DaoDailyWeatherDataResponse.Loading)
    var dayList: StateFlow<DaoDailyWeatherDataResponse> = _dayList

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
                .catch {
                    _weather.value = DaoWeatherResponse.Failure(it)
                }.collect {
                    _weather.value = DaoWeatherResponse.Success(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherHours()
                .catch {
                    _hourList.value = DaoHourlyWeatherResponse.Failure(it)
                }.collect {
                    _hourList.value = DaoHourlyWeatherResponse.Success(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherDays()
                .catch {
                    _dayList.value = DaoDailyWeatherDataResponse.Failure(it)
                }.collect {
                    _dayList.value = DaoDailyWeatherDataResponse.Success(it)
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