package com.example.weatherforecast.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherforecast.Helpers.getCountryFlagUrl
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.LocalViewModel
import com.example.weatherforecast.ViewModel.LocalViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentWeatherDetailsBinding

class WeatherDetailsFragment : Fragment() {

    lateinit var viewModel: LocalViewModel
    lateinit var binding: FragmentWeatherDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = LocalViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LocalViewModel::class.java)

        viewModel.weather.observe(viewLifecycleOwner){
                item -> setData(item)
        }
    }

    private fun setData(weather: WeatherData){
        binding.apply {
            val flagUrl = weather.countryCode?.let { getCountryFlagUrl(it) }
            Glide.with(requireContext()).load(flagUrl).into(flagImg)
            val imgUrl = getWeatherIconUrl(weather.weatherIcon)
            Glide.with(requireContext()).load(imgUrl).into(image)

            cityName.text = weather.cityName
            temperature.text = weather.temperature.toString()
            description.text = weather.weatherDescription
            humidity.text = weather.humidity.toString()
            cloud.text = weather.cloudiness.toString()
            wind.text = weather.wind.toString()
            pressure.text = weather.pressure.toString()
        }
    }

}