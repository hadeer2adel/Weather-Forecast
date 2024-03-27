package com.example.weatherforecast.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DaoNotificationResponse
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class LocationViewModel(val repository: Repository) : ViewModel (){

    private var _locationList = MutableStateFlow<DaoLocationResponse>(DaoLocationResponse.Loading)
    var locationList: StateFlow<DaoLocationResponse> = _locationList

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
                    _locationList.value = DaoLocationResponse.Failure(it)
                }.collect {
                    _locationList.value = DaoLocationResponse.Success(it)
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