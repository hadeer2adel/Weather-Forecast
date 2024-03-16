package com.example.weatherforecast.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.weatherforecast.Helpers.getCountryFlagUrl
import com.example.weatherforecast.Helpers.getFromSharedPreferences
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.Helpers.getWeatherImageUrl
import com.example.weatherforecast.Helpers.saveOnSharedPreferences
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.DayAdapter
import com.example.weatherforecast.RecycleView.HourAdapter
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.databinding.FragmentWeatherDetailsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HomeFragment : Fragment(){

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var viewModel: RemoteViewModel

    var temperatureUnits = "K"
    var windUnits = "m/s"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHourRecyclerView()
        setUpDayRecyclerView()
        initViewModel()
        getData(requireContext())
    }

    private fun setUpHourRecyclerView(){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.HORIZONTAL
        binding.todayRecyclerView.layoutManager = manager

        hourAdapter = HourAdapter(context)
        hourAdapter.submitList(emptyList())
        binding.todayRecyclerView.adapter = hourAdapter
    }
    private fun setUpDayRecyclerView(){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.weekRecyclerView.layoutManager = manager

        dayAdapter = DayAdapter(context)
        dayAdapter.submitList(emptyList())
        binding.weekRecyclerView.adapter = dayAdapter
    }
    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = RemoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(RemoteViewModel::class.java)

        viewModel.weather.observe(viewLifecycleOwner){
                item -> setDataOnView(item)
        }
        viewModel.hourlyWeatherList.observe(viewLifecycleOwner){
            items -> hourAdapter.submitList(items)
            hourAdapter.notifyDataSetChanged()
        }
        viewModel.dayList.observe(viewLifecycleOwner){
                items -> dayAdapter.submitList(items)
            dayAdapter.notifyDataSetChanged()
        }
    }
    private fun getData(context: Context){
        val latitude = getFromSharedPreferences(context, "latitude", "0").toDouble()
        val longitude = getFromSharedPreferences(context, "longitude", "0").toDouble()
        val language =  getFromSharedPreferences(context, "language", "eg")
        temperatureUnits = getFromSharedPreferences(context, "temperatureUnit", temperatureUnits)
        windUnits =  getFromSharedPreferences(context, "windUnit", windUnits)
        val units = getUnits(temperatureUnits, windUnits)

        viewModel.getCurrentWeather(latitude, longitude, units, language)
        viewModel.getHourlyWeather(latitude, longitude, units, language)
        hourAdapter.notifyDataSetChanged()
        viewModel.getDailyWeather(latitude, longitude, units, language)
        dayAdapter.notifyDataSetChanged()
    }
    private fun setDataOnView(weather: WeatherData){
        binding.apply {
            val flagUrl = weather.countryCode?.let { getCountryFlagUrl(it) }
            Glide.with(requireContext()).load(flagUrl).into(flagImg)
            val imgUrl = getWeatherIconUrl(weather.weatherIcon)
            Glide.with(requireContext()).load(imgUrl).into(image)

            time.text = weather.Time
            date.text = weather.Date
            cityName.text = weather.cityName
            temperature.text = weather.temperature.toString()
            temperatureUnit.text = "º$temperatureUnits"
            description.text = weather.weatherDescription
            humidity.text = weather.humidity.toString()
            cloud.text = weather.cloudiness.toString()
            wind.text = weather.wind.toString()
            windUnit.text = windUnits
            pressure.text = weather.pressure.toString()
        }
    }

}