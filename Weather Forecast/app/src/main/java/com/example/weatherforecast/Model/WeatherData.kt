package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.Helpers.convertTimeTo12HourFormat
import com.example.weatherforecast.Helpers.convertTimestampToDate
import com.example.weatherforecast.Helpers.convertTimestampToTime
import com.example.weatherforecast.Helpers.getDayOfWeek


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
    val minTemperature: Long,
    val maxTemperature: Long,
    val weatherIcon: String
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

    for (hourlyData in forecastWeather.list){
        val day = hourlyData.dateTimeText.split(" ")
        if (todayDate.equals(day[0]))
            hourlyWeather.add(HourlyWeatherData(convertTimeTo12HourFormat(day[1]),
                hourlyData.main.temperature.toLong(),
                hourlyData.weather.get(0).icon))
        else
            break
    }
    return hourlyWeather
}

fun getDailyWeatherData(forecastWeather: ForecastWeatherResponse): List<DailyWeatherData> {
    var hourlyWeather = ArrayList<DailyWeatherData>()

    for (hourlyData in forecastWeather.list){
        val day = hourlyData.dateTimeText.split(" ")
        if (day[1].equals("00:00:00"))
            hourlyWeather.add(DailyWeatherData(getDayOfWeek(day[0]),
                hourlyData.main.minTemperature.toLong(),
                hourlyData.main.maxTemperature.toLong(),
                hourlyData.weather.get(0).icon))
    }
    return hourlyWeather
}