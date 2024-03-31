package com.example.weatherforecast.Helpers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import java.io.IOException
import java.util.Locale

fun getUnits(temperatureUnit: String, windUnit: String): String{
    var units = "standard"

    if (temperatureUnit.equals("K") && windUnit.equals("m/s"))
        units = "standard"
    else if (temperatureUnit.equals("C") && windUnit.equals("m/s"))
        units = "metric"
    else if (temperatureUnit.equals("F") && windUnit.equals("mi/hr"))
        units = "imperial"

    return units
}

data class City(
    val cityName: String,
    val countryCode: String
)

fun getCity(context: Context, latitude: Double, longitude: Double): City {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses!!.isNotEmpty()) {
            val address: Address = addresses[0]
            var cityName = "${address.locality}, ${address.adminArea}, ${address.countryName}"
            if (address.locality == null)
                cityName = "${address.adminArea}, ${address.countryName}"
            val countryCode = address.countryCode
            return City(cityName, countryCode)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Geocoding failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
    return City("", "")
}