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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.LocalDataSource.DaoLocationResponse
import com.example.weatherforecast.LocalDataSource.DaoNotificationResponse
import com.example.weatherforecast.NotificationUtil.NotificationPermission
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.getLocationData
import com.example.weatherforecast.NotificationUtil.NotificationManagement
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.NotificationAdapter
import com.example.weatherforecast.RemoteDataSource.ApiCurrentWeatherResponse
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.NotificationViewModel
import com.example.weatherforecast.ViewModel.NotificationViewModelFactory
import com.example.weatherforecast.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlarmListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: NotificationAdapter
    lateinit var viewModel: NotificationViewModel
    lateinit var notificationPermission: NotificationPermission
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
        setUpRecyclerView(requireContext())
        initViewModel()
        notificationPermission = NotificationPermission(requireContext())

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
            findNavController().navigate(R.id.action_mainFragment_to_alarmFragment)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == My_NOTIFICATION_PERMISSION_ID) {
            if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!AppSettings.getInstance(requireContext()).notification){
                    notificationPermission.showMyAppSettingDialog(findNavController())
                }else {
                    findNavController().navigate(R.id.action_mainFragment_to_alarmFragment)
                }
            }
        }
    }

    private fun setUpRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onClick: (notification: NotificationData) -> Unit = { notification ->
            viewModel.deleteNotification(notification)
            val notificationManagement = NotificationManagement()
            notificationManagement.cancelAlarm(requireContext(), notification)
        }
        adapter = NotificationAdapter(context, onClick)
        adapter.submitList(emptyList())
        binding.recycleView.adapter = adapter
    }
    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = NotificationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)
        handleDaoNotificationResponse()
    }

    private fun handleDaoNotificationResponse(){
        lifecycleScope.launch {
            viewModel.notificationList.collectLatest { response ->
                when(response){
                    is DaoNotificationResponse.Loading -> { onLoading() }
                    is DaoNotificationResponse.Success ->{
                        onSuccess()
                        adapter.submitList(response.data)
                        adapter.notifyDataSetChanged()
                    }
                    is DaoNotificationResponse.Failure ->{ onFailure(response.error.message) }
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