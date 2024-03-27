package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.ApiForecastWeatherResponse
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class RemoteViewModel(private var repository: Repository) : ViewModel (){

    private var _weather = MutableStateFlow<ApiCurrentWeatherResponse>(ApiCurrentWeatherResponse.Loading)
    var weather: MutableStateFlow<ApiCurrentWeatherResponse> = _weather

    private var _weatherList = MutableStateFlow<ApiForecastWeatherResponse>(ApiForecastWeatherResponse.Loading)
    var weatherList: MutableStateFlow<ApiForecastWeatherResponse> = _weatherList

    fun getCurrentWeather(latitude: Double, longitude: Double, units: String, language: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getCurrentWeather(latitude, longitude, units, language)
                .catch {
                    _weather.value = ApiCurrentWeatherResponse.Failure(it)
                }.collect {
                    _weather.value = ApiCurrentWeatherResponse.Success(it!!)
                }
        }
    }

    fun getForecastWeather(latitude: Double, longitude: Double, units: String, language: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getForecastWeather(latitude, longitude, units, language)
                .catch {
                    _weatherList.value = ApiForecastWeatherResponse.Failure(it)
                }.collect {
                    _weatherList.value = ApiForecastWeatherResponse.Success(it!!)
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