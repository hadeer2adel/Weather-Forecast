package com.example.weatherforecast.ViewModel

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationViewModel(val repository: Repository) : ViewModel (){

    private var _notificationList = MutableLiveData<List<NotificationData>>()
    var notificationList: LiveData<List<NotificationData>> = _notificationList

    init {
        getAllNotifications()
    }

    fun insertNotification(notification: NotificationData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertNotification(notification)
        }
    }

    fun deleteNotification(notification: NotificationData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteNotification(notification)
            getAllNotifications()
        }
    }

    fun getAllNotifications(){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllNotifications()
                .collect {
                    _notificationList.postValue(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

class NotificationViewModelFactory (val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationViewModel::class.java)){
            NotificationViewModel(repository) as T
        }else{
            throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}