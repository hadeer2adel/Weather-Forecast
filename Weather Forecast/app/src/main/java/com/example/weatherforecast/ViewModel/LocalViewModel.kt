package com.example.weatherforecast.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocalViewModel(val repository: Repository) : ViewModel (){

    private var _locationList = MutableLiveData<List<LocationData>>()
    var locationList: LiveData<List<LocationData>> = _locationList

    init {
        getAllLocations()
    }

    fun insertLocation(location: LocationData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertLocation(location)
            getAllLocations()
        }
    }

    fun deleteLocation(location: LocationData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteLocation(location)
            getAllLocations()
        }
    }

    fun deleteAllLocations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllLocations()
        }
    }

    fun getAllLocations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllLocations()
                .collect {
                    _locationList.postValue(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}