package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.LocalDataSource.DaoAlertResponse
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class SettingViewModel(val repository: Repository) : ViewModel (){

    private var _alertList = MutableStateFlow<DaoAlertResponse>(DaoAlertResponse.Loading)
    var alertList: StateFlow<DaoAlertResponse> = _alertList

    fun deleteAllLocations(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllLocations()
        }
    }

    fun deleteAllAlerts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllAlerts()
        }
    }

    fun getAllAlerts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllAlerts()
                .catch {
                    _alertList.value = DaoAlertResponse.Failure(it)
                }.collect {
                    _alertList.value = DaoAlertResponse.Success(it)
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}

class SettingViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}