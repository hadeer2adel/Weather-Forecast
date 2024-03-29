package com.example.weatherforecast.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.R
import com.example.weatherforecast.ViewModel.LocationViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var remoteViewModel: RemoteViewModel

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private lateinit var appSettings: AppSettings
    private var latitude:Double? = null
    private var longitude:Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appSettings = AppSettings.getInstance(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.saveLocationBtn.setOnClickListener {
            if(latitude != null && longitude != null){
                val screen = requireArguments().getSerializable("Screen") as Screen
                when(screen){
                    Screen.SETTINGS -> toSettingScreen()
                    Screen.LOCATION_LIST -> toFavouriteScreen()
                    Screen.ALARM -> toAlarmScreen()
                    else -> Log.i("TAG", "onLocationResult: ")
                }
            }
        }
    }

    override fun onMapReady(_googleMap: GoogleMap) {
        googleMap = _googleMap
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

            latitude = latLng.latitude
            longitude = latLng.longitude
        }

        latitude = appSettings.latitude
        longitude = appSettings.longitude
        val currentLocation = LatLng(latitude!!, longitude!!)
        googleMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 8f))
    }

    private fun toSettingScreen(){
        val args = Bundle().apply {
            putString("latitude", latitude.toString())
            putString("longitude", longitude.toString())
            putString("locationMethod", "map")
        }
        findNavController().navigate(R.id.action_mapFragment_to_settingFragment, args)
    }
    private fun toFavouriteScreen(){
        val args = Bundle().apply {
            putString("latitude", latitude.toString())
            putString("longitude", longitude.toString())
            putInt("tabNumber", 2)
        }
        findNavController().navigate(R.id.action_mapFragment_to_mainFragment, args)
    }
    private fun toAlarmScreen(){
        val args = Bundle().apply {
            putString("latitude", latitude.toString())
            putString("longitude", longitude.toString())
            putString("locationMethod", requireContext().getString(R.string.location_2))
        }
        findNavController().navigate(R.id.action_mapFragment_to_alertFragment, args)
    }


}