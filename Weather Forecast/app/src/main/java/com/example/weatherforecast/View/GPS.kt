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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Helpers.isSharedPreferencesContains
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentLoadingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GPS : Fragment() {

    private lateinit var fusedClient : FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var locationCallback : LocationCallback
    private val My_LOCATION_PERMISSION_ID = 5005
    private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoadingBinding.inflate(inflater, container, false).root
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(checkPermissions()){
            if (isLocationEnabled()){
                getFreshLocation()
            }
            else{
                enableLocationServices(requireContext())
            }
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                My_LOCATION_PERMISSION_ID)
            showSettingDialog(requireContext())
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
        if (( ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ||( ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            return true
        }
        return false
    }

    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)){
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation(){
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(0).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                location = locationResult.lastLocation
                if (location != null){
                    fusedClient.removeLocationUpdates(this)
                    val screen = requireArguments().getSerializable("Screen") as Screen
                    when(screen){
                        Screen.SETTINGS -> toSettingScreen()
                        else -> Log.i("TAG", "onLocationResult: ")
                    }
                }
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    private fun showSettingDialog(context: Context) {
        MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle(context.getString(R.string.location_permission_title))
            .setMessage(context.getString(R.string.location_permission_body))
            .setPositiveButton(context.getString(R.string.allow)) { _, _ -> enableLocationServices(context) }
            .setNegativeButton(context.getString(R.string.cancel), null)
            .show()
    }
    private fun enableLocationServices(context: Context){
        Toast.makeText(context, context.getString(R.string.turnon_location), Toast.LENGTH_LONG)
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun toSettingScreen(){
        val args = Bundle().apply {
            putString("latitude", location?.latitude.toString())
            putString("longitude", location?.longitude.toString())
            putString("locationMethod", "gps")
        }
        findNavController().navigate(R.id.action_GPS_to_settingFragment, args)
    }
}