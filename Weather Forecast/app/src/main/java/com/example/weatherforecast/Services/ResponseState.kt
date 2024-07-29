package com.example.weatherforecast.Services

sealed class ResponseState<out T>{
    class Success<T>(val data: T): ResponseState<T>()
    class Failure(val error:Throwable): ResponseState<Nothing>()
    object Loading: ResponseState<Nothing>()
}