package com.example.weatherforecast.LocalDataSource

import com.example.weatherforecast.Model.DailyWeatherData
import com.example.weatherforecast.Model.HourlyWeatherData
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.Model.WeatherData

sealed class DaoLocationResponse {
    class Success(val data: List<LocationData>): DaoLocationResponse()
    class Failure(val error:Throwable): DaoLocationResponse()
    object Loading: DaoLocationResponse()
}

sealed class DaoAlertResponse {
    class Success(val data: List<AlertData>): DaoAlertResponse()
    class Failure(val error:Throwable): DaoAlertResponse()
    object Loading: DaoAlertResponse()
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