package com.example.weatherforecast.View

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.Helpers.getCountryFlagUrl
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.Helpers.getWeatherIconUrl
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Model.getDailyWeatherData
import com.example.weatherforecast.Model.getHourlyWeatherData
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.RecycleView.DayAdapter
import com.example.weatherforecast.RecycleView.HourAdapter
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.ApiForecastWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(){

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var viewModel: RemoteViewModel
    private lateinit var appSettings: AppSettings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSettings = AppSettings.getInstance(requireContext())

        setUpHourRecyclerView(requireContext())
        setUpDayRecyclerView(requireContext())
        initViewModel()
        getData()
    }

    private fun setUpHourRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.HORIZONTAL
        binding.todayRecyclerView.layoutManager = manager

        hourAdapter = HourAdapter(context)
        hourAdapter.submitList(emptyList())
        binding.todayRecyclerView.adapter = hourAdapter
    }
    private fun setUpDayRecyclerView(context: Context){
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
    }
    private fun getData(){
        viewModel.getCurrentWeather(appSettings)
        viewModel.getForecastWeather(appSettings)

        handleCurrentWeatherResponse()
        handleForecastWeatherResponse()
    }
    private fun handleCurrentWeatherResponse(){
        lifecycleScope.launch {
            viewModel.weather.collectLatest {response ->
                when(response){
                    is ApiCurrentWeatherResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.GONE
                    }
                    is ApiCurrentWeatherResponse.Success ->{
                        binding.progressBar.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        val weather = getWeatherData(response.data, true)
                        setDataOnView(weather)
                    }
                    is ApiCurrentWeatherResponse.Failure ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), response.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun handleForecastWeatherResponse(){
        lifecycleScope.launch {
            viewModel.weatherList.collectLatest {response ->
                when(response){
                    is ApiForecastWeatherResponse.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView.visibility = View.GONE
                    }
                    is ApiForecastWeatherResponse.Success ->{
                        binding.progressBar.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        val hourlyWeatherData = getHourlyWeatherData(response.data)
                        hourAdapter.submitList(hourlyWeatherData)
                        hourAdapter.notifyDataSetChanged()
                        val dailyWeatherData = getDailyWeatherData(response.data, AppSettings.getInstance(requireContext()).language )
                        dayAdapter.submitList(dailyWeatherData)
                        dayAdapter.notifyDataSetChanged()
                    }
                    is ApiForecastWeatherResponse.Failure ->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), response.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
            temperatureUnit.text = "º${appSettings.temperatureUnit}"
            description.text = weather.weatherDescription
            humidity.text = weather.humidity.toString()
            cloud.text = weather.cloudiness.toString()
            wind.text = weather.wind.toString()
            windUnit.text = appSettings.windUnit
            pressure.text = weather.pressure.toString()
        }
    }

}