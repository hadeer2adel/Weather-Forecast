package com.example.weatherforecast.Helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertTimestampToDate(timestamp: Long, timezone: Long): String {
    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("MMM, d", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val localTimeZone = TimeZone.getDefault()
    val offset = localTimeZone.getOffset(timestamp * 1000) + timezone * 1000
    sdf.timeZone = TimeZone.getTimeZone("GMT${if (offset >= 0) "+" else "-"}${(Math.abs(offset) / 3600000).toString().padStart(2, '0')}:${(Math.abs(offset) % 3600000 / 60000).toString().padStart(2, '0')}")

    return sdf.format(date)
}

fun convertTimestampToTime(timestamp: Long, timezone: Long): String {
    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val localTimeZone = TimeZone.getDefault()
    val offset = localTimeZone.getOffset(timestamp * 1000) + timezone * 1000
    sdf.timeZone = TimeZone.getTimeZone("GMT${if (offset >= 0) "+" else "-"}${(Math.abs(offset) / 3600000).toString().padStart(2, '0')}:${(Math.abs(offset) % 3600000 / 60000).toString().padStart(2, '0')}")

    return sdf.format(date)
}

fun convertTimeTo12HourFormat(timeString: String): String {
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h a", Locale.getDefault())
    val date = inputFormat.parse(timeString)
    return outputFormat.format(date)
}

fun getDayOfWeek(date: String):String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(date)
    val calendar = Calendar.getInstance()
    calendar.time = date

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val dayOfWeekString = when (dayOfWeek) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> ""
    }

    return dayOfWeekString
}