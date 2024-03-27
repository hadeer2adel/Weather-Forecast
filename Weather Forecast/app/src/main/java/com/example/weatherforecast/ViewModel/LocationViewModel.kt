package com.example.weatherforecast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationViewModel(val repository: Repository) : ViewModel (){

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

class LocationViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationViewModel::class.java)){
            LocationViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}