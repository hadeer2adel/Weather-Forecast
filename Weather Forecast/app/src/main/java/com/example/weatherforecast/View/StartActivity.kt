package com.example.weatherforecast.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecast.Helpers.saveOnSharedPreferences
import com.example.weatherforecast.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class StartActivity : AppCompatActivity() {

    private lateinit var fusedClient : FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var locationCallback : LocationCallback
    private val My_LOCATION_PERMISSION_ID = 5005
    private var location: Location? = null

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(checkPermissions()){
            if (isLocationEnabled()){
                getFreshLocation()
            }
            else{
                enableLocationServices()
            }
        }
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                My_LOCATION_PERMISSION_ID)
        }
    }

    override fun onRequestPermissionsResult( requestCode:Int, permissions:Array<String>, grantResults:IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID) {
            if (!grantResults.isEmpty() && checkPermissions()) {
                getFreshLocation()
            }
        }
    }

    private fun checkPermissions():Boolean{
        if (( ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ||( ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            return true
        }
        return false
    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)){
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation(){
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                location = locationResult.lastLocation
                if (location != null){
                    val latitude = location?.latitude.toString()
                    val longitude = location?.longitude.toString()
                    saveOnSharedPreferences(this@StartActivity, "latitude", latitude)
                    saveOnSharedPreferences(this@StartActivity, "longitude", longitude)
                    fusedClient.removeLocationUpdates(this)
                    val intent = Intent(this@StartActivity, SplashScreenActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun enableLocationServices(){
        Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG)
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun getAddress(location: Location): String {
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address =  addresses?.get(0)?.getAddressLine(0).toString()
        return address
    }
}