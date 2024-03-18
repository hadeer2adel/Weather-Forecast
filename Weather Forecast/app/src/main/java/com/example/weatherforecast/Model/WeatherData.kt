package com.example.weatherforecast.Model

import android.os.Build
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.Helpers.convertTimeTo12HourFormat
import com.example.weatherforecast.Helpers.convertTimestampToDate
import com.example.weatherforecast.Helpers.convertTimestampToTime
import com.example.weatherforecast.Helpers.getDayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "Weather")
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val currentLocation: Boolean,
    val latitude: Double,
    val longitude: Double,
    val Date: String,
    val Time: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Long,
    val humidity: Int,
    val pressure: Long,
    val wind: Double,
    val cloudiness: Int,
    val cityName: String,
    val countryCode: String?,
)

data class HourlyWeatherData(
    val time: String,
    val temperature: Long,
    val weatherIcon: String
)

data class DailyWeatherData(
    val date: String,
    var minTemperature: Long,
    var maxTemperature: Long,
    var weatherIcon: String
)

fun getWeatherData(weather: CurrentWeatherResponse, isCurrentLocation: Boolean): WeatherData{
    return WeatherData(
        0,
        isCurrentLocation,
        weather.coordinates.latitude,
        weather.coordinates.longitude,
        convertTimestampToDate(weather.timestamp, weather.timezone),
        convertTimestampToTime(weather.timestamp, weather.timezone),
        weather.weather.get(0).description,
        weather.weather.get(0).icon,
        weather.main.temperature.toLong(),
        weather.main.humidity,
        weather.main.pressure.toLong(),
        weather.wind.speed,
        weather.clouds.cloudiness,
        weather.cityName,
        weather.country.code
    )
}

fun getHourlyWeatherData(forecastWeather: ForecastWeatherResponse): List<HourlyWeatherData> {
    var hourlyWeather = ArrayList<HourlyWeatherData>()
    val today = forecastWeather.list.get(0).dateTimeText.split(" ")
    val todayDate = today[0]

    for ((hourlyData, nextHourlyData) in forecastWeather.list.zipWithNext()){
        val hour = hourlyData.dateTimeText.split(" ")
        val nextHour = nextHourlyData.dateTimeText.split(" ")
        val temperatureRate = (nextHourlyData.main.temperature - hourlyData.main.temperature)/3
        if (todayDate.equals(hour[0]) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            var startTime = LocalTime.parse(hour[1], formatter)
            val endTime = LocalTime.parse(nextHour[1], formatter)
            var i = 1
            while (startTime.isBefore(endTime)) {
                hourlyWeather.add(
                    HourlyWeatherData(
                        convertTimeTo12HourFormat(startTime.format(formatter)),
                        (hourlyData.main.temperature + temperatureRate*i).toLong(),
                        hourlyData.weather.get(0).icon
                    )
                )
                startTime = startTime.plusHours(1)
                i++
            }
        }
        else
            break
    }
    return hourlyWeather
}

fun getDailyWeatherData(forecastWeather: ForecastWeatherResponse, language: String): List<DailyWeatherData> {
    var dailyWeather = ArrayList<DailyWeatherData>()
    var index = -1

    for (dailyData in forecastWeather.list){
        val day = dailyData.dateTimeText.split(" ")
        if (day[1].equals("00:00:00")) {
            index++
            dailyWeather.add(
                DailyWeatherData(
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