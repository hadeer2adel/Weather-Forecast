package com.example.weatherforecast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private var _dayList = MutableLiveData<List<DailyWeatherData>>()
    var dayList: LiveData<List<DailyWeatherData>> = _dayList

    fun getCurrentWeather(appSettings: AppSettings){
        viewModelScope.launch(Dispatchers.IO){
            repository.getCurrentWeather(appSettings.latitude, appSettings.longitude, getUnits(appSettings.temperatureUnit, appSettings.windUnit), appSettings.language)
                .catch {
                    _weather.value = ApiCurrentWeatherResponse.Failure(it)
                }.collect {
                    _weather.value = ApiCurrentWeatherResponse.Success(it!!)
                }
        }
    }

    fun getForecastWeather(appSettings: AppSettings){
        viewModelScope.launch(Dispatchers.IO){
            repository.getForecastWeather(appSettings.latitude, appSettings.longitude, getUnits(appSettings.temperatureUnit, appSettings.windUnit), appSettings.language)
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