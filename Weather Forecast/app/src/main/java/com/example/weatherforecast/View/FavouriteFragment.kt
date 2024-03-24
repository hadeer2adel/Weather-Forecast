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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.Model.getLocationData
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.LocationAdapter
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.LocalViewModel
import com.example.weatherforecast.ViewModel.LocalViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: LocationAdapter
    private lateinit var localViewModel: LocalViewModel
    private lateinit var remoteViewModel: RemoteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(requireContext())
        initViewModel()

        if(arguments != null && requireArguments().containsKey("latitude")) {
            val appSettings = AppSettings.getInstance(requireContext())
            val latitude = requireArguments().getString("latitude", "0").toDouble()
            val longitude = requireArguments().getString("longitude", "0").toDouble()

            val units = getUnits(appSettings.temperatureUnit, appSettings.windUnit)
            remoteViewModel.getCurrentWeather(latitude, longitude, units, appSettings.language)
            handleCurrentWeatherResponse()
        }

        binding.addBtn.setOnClickListener {
            val args = Bundle().apply {
                putSerializable("Screen", Screen.FAVOURITE)
            }
            findNavController().navigate(R.id.action_mainFragment_to_mapFragment, args)
        }
    }

    private fun setUpRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onClick: (location: LocationData) -> Unit = { location ->
            localViewModel.deleteLocation(location)
        }
        val onCardClick: (location: LocationData) -> Unit = { location ->
            val args = Bundle().apply {
                putString("latitude", location.latitude.toString())
                putString("longitude", location.longitude.toString())
            }
            findNavController().navigate(R.id.action_mainFragment_to_locationDetailsFragment, args)
        }
        adapter = LocationAdapter(context, View.VISIBLE, onClick, onCardClick)
        adapter.submitList(emptyList())
        binding.recycleView.adapter = adapter
    }
    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val remoteFactory = RemoteViewModelFactory(repository)
        remoteViewModel = ViewModelProvider(this, remoteFactory).get(RemoteViewModel::class.java)

        val localFactory = LocalViewModelFactory(repository)
        localViewModel = ViewModelProvider(this, localFactory).get(LocalViewModel::class.java)
        localViewModel.locationList.observe(viewLifecycleOwner){
                locations -> adapter.submitList(locations)
            adapter.notifyDataSetChanged()
        }
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