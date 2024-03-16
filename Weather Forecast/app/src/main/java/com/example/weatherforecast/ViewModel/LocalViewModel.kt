package com.example.weatherforecast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocalViewModel(val repository: Repository) : ViewModel (){

    private var _locationList = MutableLiveData<List<WeatherData>>()
    var locationList: LiveData<List<WeatherData>> = _locationList

    private var _weather = MutableLiveData<WeatherData>()
    var weather: LiveData<WeatherData> = _weather

    init {
        getAllLocations()
    }

    fun getLastWeather(){
        viewModelScope.launch(Dispatchers.IO){
            _weather.postValue(repository.getLastWeather())
        }
    }

    fun insertLocation(location: WeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLocation(location)
            getAllLocations()
        }
    }

    fun deleteLocation(location: WeatherData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteLocation(location)
            getAllLocations()
        }
    }

    fun getAllLocations(){
        viewModelScope.launch(Dispatchers.IO){
            val locations = repository.getAllLocations()
            _locationList.postValue(locations)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}