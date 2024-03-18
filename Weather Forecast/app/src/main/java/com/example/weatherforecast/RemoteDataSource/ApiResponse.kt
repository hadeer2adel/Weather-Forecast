package com.example.weatherforecast.RemoteDataSource

import com.example.weatherforecast.Model.CurrentWeatherResponse
import com.example.weatherforecast.Model.ForecastWeatherResponse

sealed class ApiCurrentWeatherResponse {
    class Success(val data: CurrentWeatherResponse): ApiCurrentWeatherResponse()
    class Failure(val error:Throwable): ApiCurrentWeatherResponse()
    object Loading: ApiCurrentWeatherResponse()
}

sealed class ApiForecastWeatherResponse {
    class Success(val data: ForecastWeatherResponse): ApiForecastWeatherResponse()
    class Failure(val error:Throwable): ApiForecastWeatherResponse()
    object Loading: ApiForecastWeatherResponse()
}