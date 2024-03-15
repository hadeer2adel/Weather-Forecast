package com.example.weatherforecast.Helpers

import android.health.connect.datatypes.units.Temperature

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