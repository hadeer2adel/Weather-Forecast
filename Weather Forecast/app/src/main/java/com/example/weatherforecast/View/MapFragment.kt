package com.example.weatherforecast.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

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
                appSettings.latitude = latitude as Double
                appSettings.longitude = longitude as Double
                appSettings.locationMethod = "map"
                findNavController().navigate(R.id.action_mapFragment_to_settingFragment)
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))
    }

}