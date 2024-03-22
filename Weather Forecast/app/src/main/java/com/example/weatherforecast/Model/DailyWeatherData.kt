package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.Helpers.getDayOfWeek

@Entity(tableName = "Days")
data class DailyWeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    var minTemperature: Long,
    var maxTemperature: Long,
    var weatherIcon: String
)

fun getDailyWeatherData(forecastWeather: ForecastWeatherResponse, language: String): List<DailyWeatherData> {
    var dailyWeather = ArrayList<DailyWeatherData>()
    var index = -1

    for (dailyData in forecastWeather.list){
        val day = dailyData.dateTimeText.split(" ")
        if (day[1].equals("00:00:00")) {
            index++
            dailyWeather.add(
                DailyWeatherData(
                    0,
                    getDayOfWeek(day[0], language),
                    dailyData.main.minTemperature.toLong(),
                    dailyData.main.maxTemperature.toLong(),
                    dailyData.weather.get(0).icon
                )
            )
        }
        if(index != -1 && dailyData.main.temperature.toLong() < dailyWeather.get(index).minTemperature){
            dailyWeather.get(index).minTemperature = dailyData.main.temperature.toLong()
        }
        if(index != -1 && dailyData.main.temperature.toLong() > dailyWeather.get(index).maxTemperature){
            dailyWeather.get(index).maxTemperature = dailyData.main.temperature.toLong()
            dailyWeather.get(index).weatherIcon = dailyData.weather.get(0).icon
        }

    }
    return dailyWeather
}