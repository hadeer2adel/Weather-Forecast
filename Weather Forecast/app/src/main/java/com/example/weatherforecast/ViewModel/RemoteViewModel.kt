package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class RemoteViewModel(private var repository: Repository) : ViewModel (){

    private var _weather = MutableStateFlow<ResponseState<CurrentWeatherResponse>>(ResponseState.Loading)
    var weather: StateFlow<ResponseState<CurrentWeatherResponse>> = _weather

    private var _weatherList = MutableStateFlow<ResponseState<ForecastWeatherResponse>>(
        ResponseState.Loading)
    var weatherList: StateFlow<ResponseState<ForecastWeatherResponse>> = _weatherList

    fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getCurrentWeather(latitude, longitude, units, language)
                .catch {
                    _weather.value = ResponseState.Failure(it)
                }.collect {
                    _weather.value = ResponseState.Success(it!!)
                }
        }
    }

    fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getForecastWeather(latitude, longitude, units, language)
                .catch {
                    _weatherList.value = ResponseState.Failure(it)
                }.collect {
                    _weatherList.value = ResponseState.Success(it!!)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

class RemoteViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RemoteViewModel::class.java)){
            RemoteViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}