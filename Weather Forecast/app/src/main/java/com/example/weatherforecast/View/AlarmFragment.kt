package com.example.weatherforecast.View

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.NotificationUtil.NotificationPermission
import com.example.weatherforecast.NotificationUtil.NotificationSelector
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.LocationData
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.LocationAdapter
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.LocalViewModel
import com.example.weatherforecast.ViewModel.LocalViewModelFactory
import com.example.weatherforecast.databinding.FragmentListBinding

class AlarmFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: LocationAdapter
    lateinit var viewModel: LocalViewModel
    lateinit var notificationPermission: NotificationPermission
    lateinit var notificationSelector: NotificationSelector
    private val My_NOTIFICATION_PERMISSION_ID = 202

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationPermission = NotificationPermission(requireContext())
        notificationSelector = NotificationSelector(requireContext())

        if(arguments != null && requireArguments().containsKey("latitude")) {
            val latitude = requireArguments().getString("latitude", "0")
            val longitude = requireArguments().getString("longitude", "0")
            notificationSelector.selectDate(childFragmentManager, latitude, longitude)
        }

        binding.addBtn.setOnClickListener {
            checkNotificationDevicePermission(requireContext(), requireActivity(), findNavController())
        }
    }

    private fun checkNotificationDevicePermission(context: Context, activity: Activity, navController: NavController){
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity,
                arrayOf( Manifest.permission.POST_NOTIFICATIONS ),
                My_NOTIFICATION_PERMISSION_ID)
            notificationPermission.showDeviceSettingDialog()
        } else if(!AppSettings.getInstance(context).notification){
            notificationPermission.showMyAppSettingDialog(navController)
        }else {
            notificationSelector.selectLocation(childFragmentManager, navController)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == My_NOTIFICATION_PERMISSION_ID) {
            if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!AppSettings.getInstance(requireContext()).notification){
                    notificationPermission.showMyAppSettingDialog(findNavController())
                }else {
                    notificationSelector.selectLocation(childFragmentManager, findNavController())
                }            }
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