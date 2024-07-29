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
import com.example.weatherforecast.Helpers.getWeatherIcon
import com.example.weatherforecast.Helpers.isNetworkConnected
import com.example.weatherforecast.Services.Caching.DataBase
import com.example.weatherforecast.Services.Caching.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.Model.getDailyWeatherData
import com.example.weatherforecast.Model.getHourlyWeatherData
import com.example.weatherforecast.Model.getWeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.DayAdapter
import com.example.weatherforecast.RecycleView.HourAdapter
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Services.Networking.NetworkManager
import com.example.weatherforecast.Services.Networking.NetworkManagerImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.HomeLocalViewModel
import com.example.weatherforecast.ViewModel.HomeLocalViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment(){

    lateinit var binding: FragmentHomeBinding
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var remoteViewModel: RemoteViewModel
    lateinit var localViewModel: HomeLocalViewModel
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

        if(isNetworkConnected(requireContext())) {
            localViewModel.deleteLastWeather()

            val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)
            remoteViewModel.getCurrentWeather(appSettings.latitude, appSettings.longitude, units, appSettings.language)
            remoteViewModel.getForecastWeather(appSettings.latitude, appSettings.longitude, units, appSettings.language)

            handleCurrentWeatherResponse()
            handleForecastWeatherResponse()
        }
        else {
            localViewModel.getLastWeather()
            handleResponseState()
        }
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
        val networkManager: NetworkManager = NetworkManagerImpl.getInstance()
        val dataBase: DataBase = DataBase.getInstance(requireContext())
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAOAlerts())
        val repository: Repository = RepositoryImpl(networkManager, localDataSource)

        val remoteFactory = RemoteViewModelFactory(repository)
        remoteViewModel = ViewModelProvider(this, remoteFactory).get(RemoteViewModel::class.java)

        val localFactory = HomeLocalViewModelFactory(repository)
        localViewModel = ViewModelProvider(this, localFactory).get(HomeLocalViewModel::class.java)
    }

    private fun handleResponseState(){
        lifecycleScope.launch {
            localViewModel.weather.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        if(response.data != null)
                            setDataOnView(response.data)
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
                }
            }
        }

        lifecycleScope.launch {
            localViewModel.hourList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        hourAdapter.submitList(response.data)
                        hourAdapter.notifyDataSetChanged()
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
                }
            }
        }
        
        lifecycleScope.launch {
            localViewModel.dayList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        dayAdapter.submitList(response.data)
                        dayAdapter.notifyDataSetChanged()
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
                }
            }
        }
    }
    private fun handleCurrentWeatherResponse(){
        lifecycleScope.launch {
            remoteViewModel.weather.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        val weather = getWeatherData(response.data)
                        setDataOnView(weather)
                        localViewModel.insertLastWeather(weather)
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
                }
            }
        }
    }
    private fun handleForecastWeatherResponse(){
        lifecycleScope.launch {
            remoteViewModel.weatherList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        val hourlyWeatherData = getHourlyWeatherData(response.data)
                        hourAdapter.submitList(hourlyWeatherData)
                        hourAdapter.notifyDataSetChanged()
                        val dailyWeatherData = getDailyWeatherData(response.data, AppSettings.getInstance(requireContext()).language )
                        dayAdapter.submitList(dailyWeatherData)
                        dayAdapter.notifyDataSetChanged()
                        localViewModel.insertLastWeatherHour(hourlyWeatherData)
                        localViewModel.insertLastWeatherDay(dailyWeatherData)
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
                }
            }
        }
    }
    private fun setDataOnView(weather: WeatherData){
        binding.apply {

            val flagUrl = weather.countryCode?.let { getCountryFlagUrl(it) }
            Glide.with(requireContext()).load(flagUrl).into(flagImg)

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val isMorning = (currentHour in 6..17)
            val imgUrl = getWeatherIcon(weather.weatherIcon, isMorning)
            image.setImageResource(imgUrl)

            date.text = weather.Date
            cityName.text = weather.cityName
            temperature.text = weather.temperature.toString()
            temperatureUnit.text = "º${appSettings.temperatureUnit}"
            description.text = weather.weatherDescription
            humidity.text = weather.humidity.toString()
            cloud.text = weather.cloudiness.toString()
            wind.text = weather.wind.toString()
            if (appSettings.windUnit.equals("m/s")){
                windUnit.text = getString(R.string.w_unit_1)
            }
            else {
                windUnit.text = getString(R.string.w_unit_2)
            }
            pressure.text = weather.pressure.toString()
        }
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE
    }
    private fun onSuccess(){
        binding.progressBar.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
    }
    private fun onFailure(message: String?){
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}