package com.example.weatherforecast.View

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.Helpers.getUnits
import com.example.weatherforecast.Helpers.isNetworkConnected
import com.example.weatherforecast.Helpers.showDialog
import com.example.weatherforecast.Helpers.showNetworkDialog
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DataBase
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
import com.example.weatherforecast.ViewModel.LocationViewModel
import com.example.weatherforecast.ViewModel.LocationViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocationListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: LocationAdapter
    private lateinit var locationViewModel: LocationViewModel


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
            val latitude = requireArguments().getString("latitude", "0").toDouble()
            val longitude = requireArguments().getString("longitude", "0").toDouble()
            val location = LocationData(0, latitude, longitude)
            locationViewModel.insertLocation(location)
        }

        binding.addBtn.setOnClickListener {
            if (isNetworkConnected(requireContext())) {
                val args = Bundle().apply {
                    putSerializable("Screen", Screen.LOCATION_LIST)
                }
                findNavController().navigate(R.id.action_mainFragment_to_mapFragment, args)
            }
            else{
                showNetworkDialog(requireContext())
            }
        }
    }

    private fun setUpRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onClick: (location: LocationData) -> Unit = { location ->
            val onAllow: () -> Unit = {
                locationViewModel.deleteLocation(location)
            }
            showDialog(requireContext(), R.string.delete_location_title, R.string.delete_location_body, onAllow)
        }

        val onCardClick: (location: LocationData) -> Unit = { location ->
            if (isNetworkConnected(requireContext())) {
                val args = Bundle().apply {
                    putString("latitude", location.latitude.toString())
                    putString("longitude", location.longitude.toString())
                }
                findNavController().navigate(R.id.action_mainFragment_to_locationFragment, args)
            }
            else{
                showNetworkDialog(requireContext())
            }
        }
        adapter = LocationAdapter(context, View.VISIBLE, onClick, onCardClick)
        adapter.submitList(emptyList())
        binding.recycleView.adapter = adapter
    }
    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val dataBase: DataBase = DataBase.getInstance(requireContext())
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAOAlerts())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val localFactory = LocationViewModelFactory(repository)
        locationViewModel = ViewModelProvider(this, localFactory).get(LocationViewModel::class.java)
        handleDaoLocationResponse()
    }

    private fun handleDaoLocationResponse(){
        lifecycleScope.launch {
            locationViewModel.locationList.collectLatest { response ->
                when(response){
                    is DaoLocationResponse.Loading -> { onLoading() }
                    is DaoLocationResponse.Success ->{
                        onSuccess()
                        adapter.submitList(response.data)
                        adapter.notifyDataSetChanged()
                    }
                    is DaoLocationResponse.Failure ->{ onFailure(response.error.message) }
                }
            }
        }
    }
    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.recycleView.visibility = View.GONE
        binding.addBtn.visibility = View.GONE
    }
    private fun onSuccess(){
        binding.progressBar.visibility = View.GONE
        binding.recycleView.visibility = View.VISIBLE
        binding.addBtn.visibility = View.VISIBLE
    }
    private fun onFailure(message: String?){
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}