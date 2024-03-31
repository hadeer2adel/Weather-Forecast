package com.example.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.LocalDataSource.DaoAlertResponse
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class AlertViewModel(val repository: Repository) : ViewModel (){

    private var _alertList = MutableStateFlow<DaoAlertResponse>(DaoAlertResponse.Loading)
    var alertList: StateFlow<DaoAlertResponse> = _alertList

    init {
        getAllAlerts()
    }

    fun insertAlert(alert: AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: AlertData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAlert(alert)
            getAllAlerts()
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

class AlertViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}