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
import com.example.weatherforecast.LocalDataSource.DaoNotificationResponse
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class NotificationViewModel(val repository: Repository) : ViewModel (){

    private var _notificationList = MutableStateFlow<DaoNotificationResponse>(DaoNotificationResponse.Loading)
    var notificationList: StateFlow<DaoNotificationResponse> = _notificationList

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
                .catch {
                    _notificationList.value = DaoNotificationResponse.Failure(it)
                }.collect {
                    _notificationList.value = DaoNotificationResponse.Success(it)
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