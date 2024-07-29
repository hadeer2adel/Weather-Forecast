package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class HomeLocalViewModel(val repository: Repository) : ViewModel (){

    private var _weather = MutableStateFlow<ResponseState<WeatherData>>(ResponseState.Loading)
    var weather: StateFlow<ResponseState<WeatherData>> = _weather

    private var _hourList = MutableStateFlow<ResponseState<List<HourlyWeatherData>>>(ResponseState.Loading)
    var hourList: StateFlow<ResponseState<List<HourlyWeatherData>>> = _hourList

    private var _dayList = MutableStateFlow<ResponseState<List<DailyWeatherData>>>(ResponseState.Loading)
    var dayList: StateFlow<ResponseState<List<DailyWeatherData>>> = _dayList

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
                    _weather.value = ResponseState.Failure(it)
                }.collect {
                    _weather.value = ResponseState.Success(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherHours()
                .catch {
                    _hourList.value = ResponseState.Failure(it)
                }.collect {
                    _hourList.value = ResponseState.Success(it)
                }
        }
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastWeatherDays()
                .catch {
                    _dayList.value = ResponseState.Failure(it)
                }.collect {
                    _dayList.value = ResponseState.Success(it)
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