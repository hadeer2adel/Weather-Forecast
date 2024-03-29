package com.example.weatherforecast.LocalDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.ForecastWeatherResponse
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.WeatherData

sealed class DaoLocationResponse {
    class Success(val data: List<LocationData>): DaoLocationResponse()
    class Failure(val error:Throwable): DaoLocationResponse()
    object Loading: DaoLocationResponse()
}

sealed class DaoNotificationResponse {
    class Success(val data: List<NotificationData>): DaoNotificationResponse()
    class Failure(val error:Throwable): DaoNotificationResponse()
    object Loading: DaoNotificationResponse()
}

sealed class DaoWeatherResponse {
    class Success(val data: WeatherData?): DaoWeatherResponse()
    class Failure(val error:Throwable): DaoWeatherResponse()
    object Loading: DaoWeatherResponse()
}

sealed class DaoHourlyWeatherResponse {
    class Success(val data: List<HourlyWeatherData>): DaoHourlyWeatherResponse()
    class Failure(val error:Throwable): DaoHourlyWeatherResponse()
    object Loading: DaoHourlyWeatherResponse()
}

sealed class DaoDailyWeatherDataResponse {
    class Success(val data: List<DailyWeatherData>): DaoDailyWeatherDataResponse()
    class Failure(val error:Throwable): DaoDailyWeatherDataResponse()
    object Loading: DaoDailyWeatherDataResponse()
}