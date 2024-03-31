package com.example.weatherforecast.Model

import android.os.Build
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.Helpers.convertTimeTo12HourFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "Hours")
data class HourlyWeatherData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String,
    val temperature: Long,
    val weatherIcon: String
)

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
            while (!startTime.equals(endTime)) {
                hourlyWeather.add(
                    HourlyWeatherData(
                        0,
                        convertTimeTo12HourFormat(startTime.format(formatter)),
                        (hourlyData.main.temperature + temperatureRate*i).toLong(),
                        hourlyData.weather.get(0).icon
                    )
                )
                startTime = startTime.plusHours(1)
                i++
            }
            if (nextHour[1].equals("00:00:00")){
                hourlyWeather.add(
                    HourlyWeatherData(
                        0,
                        convertTimeTo12HourFormat(endTime.format(formatter)),
                        nextHourlyData.main.temperature.toLong(),
                        nextHourlyData.weather.get(0).icon
                    )
                )
            }
        }
        else
            break
    }
    return hourlyWeather
}