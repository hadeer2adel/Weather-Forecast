package com.example.weatherforecast.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.getLocationData
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.LocalViewModel
import com.example.weatherforecast.ViewModel.LocalViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private lateinit var appSettings: AppSettings
    private lateinit var localViewModel: LocalViewModel
    private lateinit var remoteViewModel: RemoteViewModel
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
        initViewModel()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.saveLocationBtn.setOnClickListener {
            if(latitude != null && longitude != null){
                val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)
                remoteViewModel.getCurrentWeather(latitude!!, longitude!!, units, appSettings.language)
                handleCurrentWeatherResponse()
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

    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val localFactory = LocalViewModelFactory(repository)
        localViewModel = ViewModelProvider(this, localFactory).get(LocalViewModel::class.java)

        val remoteFactory = RemoteViewModelFactory(repository)
        remoteViewModel = ViewModelProvider(this, remoteFactory).get(RemoteViewModel::class.java)
    }

    private fun handleCurrentWeatherResponse(){
        lifecycleScope.launch {
            remoteViewModel.weather.collectLatest {response ->
                when(response){
                    is ApiCurrentWeatherResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ApiCurrentWeatherResponse.Success ->{
                        binding.progressBar.visibility = View.GONE
                        val location = getLocationData(response.data)
                        localViewModel.insertLocation(location)
                        val args = Bundle().apply { putInt("tabNumber", 2) }
                        val navController = findNavController()
                        navController.navigate(R.id.action_favouriteMapFragment_to_mainFragment, args)
                    }
                    is ApiCurrentWeatherResponse.Failure ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), response.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}