package com.example.weatherforecast.View

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.Model.WeatherData
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.DayAdapter
import com.example.weatherforecast.RecycleView.LocationAdapter
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.LocalViewModel
import com.example.weatherforecast.ViewModel.LocalViewModelFactory
import com.example.weatherforecast.ViewModel.RemoteViewModel
import com.example.weatherforecast.ViewModel.RemoteViewModelFactory
import com.example.weatherforecast.databinding.FragmentFavouriteBinding

class FavouriteFragment : Fragment() {
    lateinit var binding: FragmentFavouriteBinding
    lateinit var adapter: LocationAdapter
    lateinit var viewModel: LocalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(requireContext())
        initViewModel()

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_favouriteMapFragment)
        }
    }

    private fun setUpRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onClick: (location: LocationData) -> Unit = { location ->
            viewModel.deleteLocation(location)
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

        val factory = LocalViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LocalViewModel::class.java)
        viewModel.locationList.observe(viewLifecycleOwner){
                locations -> adapter.submitList(locations)
            adapter.notifyDataSetChanged()
        }
    }

}