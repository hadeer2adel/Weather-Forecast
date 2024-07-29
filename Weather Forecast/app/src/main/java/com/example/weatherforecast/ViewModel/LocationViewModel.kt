package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class LocationViewModel(val repository: Repository) : ViewModel (){

    private var _locationList = MutableStateFlow<ResponseState<List<LocationData>>>(ResponseState.Loading)
    var locationList: StateFlow<ResponseState<List<LocationData>>> = _locationList

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
                .catch {
                    _locationList.value = ResponseState.Failure(it)
                }.collect {
                    _locationList.value = ResponseState.Success(it)
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