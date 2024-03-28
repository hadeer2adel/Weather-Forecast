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
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DataBase
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
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
import com.example.weatherforecast.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedLocationsFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: LocationAdapter
    lateinit var viewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addBtn.visibility = View.GONE

        setUpRecyclerView(requireContext())
        initViewModel()
    }

    private fun setUpRecyclerView(context: Context){
        val manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onCardClick: (location: LocationData) -> Unit = { location ->
            val screen = requireArguments().getSerializable("Screen") as Screen
            when(screen){
                Screen.SETTINGS -> toSettingScreen(location)
                Screen.ALARM -> toAlarmScreen(location)
                else -> Log.i("TAG", "onLocationResult: ")
            }
        }
        adapter = LocationAdapter(context, View.GONE, {  }, onCardClick)
        adapter.submitList(emptyList())
        binding.recycleView.adapter = adapter
    }
    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val dataBase: DataBase = DataBase.getInstance(requireContext())
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAONotifications())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = LocationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LocationViewModel::class.java)
        handleDaoLocationResponse()
    }

    private fun handleDaoLocationResponse(){
        lifecycleScope.launch {
            viewModel.locationList.collectLatest { response ->
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
    }
    private fun onFailure(message: String?){
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun toSettingScreen(location: LocationData){
        val args = Bundle().apply {
            putString("latitude", location.latitude.toString())
            putString("longitude", location.longitude.toString())
            putString("locationMethod", "saved")
        }
        findNavController().navigate(R.id.action_savedLocationsFragment_to_settingFragment, args)
    }
    private fun toAlarmScreen(location: LocationData){
        val args = Bundle().apply {
            putString("latitude", location.latitude.toString())
            putString("longitude", location.longitude.toString())
            putString("locationMethod", requireContext().getString(R.string.location_3))
        }
        findNavController().navigate(R.id.action_savedLocationsFragment_to_alarmFragment, args)
    }

}