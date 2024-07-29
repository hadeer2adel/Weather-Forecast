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
import com.example.weatherforecast.Helpers.showDialog
import com.example.weatherforecast.Services.ResponseState
import com.example.weatherforecast.Services.Caching.DataBase
import com.example.weatherforecast.AlertUtil.AlertPermission
import com.example.weatherforecast.Services.Caching.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.AlertData
import com.example.weatherforecast.AlertUtil.AlertManagement
import com.example.weatherforecast.R
import com.example.weatherforecast.RecycleView.AlertAdapter
import com.example.weatherforecast.Services.Networking.NetworkManager
import com.example.weatherforecast.Services.Networking.NetworkManagerImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.AlertViewModel
import com.example.weatherforecast.ViewModel.AlertViewModelFactory
import com.example.weatherforecast.databinding.FragmentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertListFragment : Fragment() {
    lateinit var binding: FragmentListBinding
    lateinit var adapter: AlertAdapter
    lateinit var viewModel: AlertViewModel
    lateinit var alertPermission: AlertPermission
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
        alertPermission = AlertPermission(requireContext())

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
            alertPermission.showDeviceSettingDialog()
        } else if(!AppSettings.getInstance(context).notification){
            alertPermission.showMyAppSettingDialog(navController)
        }else {
            findNavController().navigate(R.id.action_mainFragment_to_alertFragment)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == My_NOTIFICATION_PERMISSION_ID) {
            if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!AppSettings.getInstance(requireContext()).notification){
                    alertPermission.showMyAppSettingDialog(findNavController())
                }else {
                    findNavController().navigate(R.id.action_mainFragment_to_alertFragment)
                }
            }
        }
    }

    private fun setUpRecyclerView(context: Context){
        var manager = LinearLayoutManager(context)
        manager.orientation = RecyclerView.VERTICAL
        binding.recycleView.layoutManager = manager

        val onClick: (alert: AlertData) -> Unit = { alert ->
            val onAllow: () -> Unit = {
                viewModel.deleteAlert(alert)
                val alertManagement = AlertManagement()
                alertManagement.cancelAlarm(requireContext(), alert)
            }
            showDialog(requireContext(), R.string.delete_alert_title, R.string.delete_alert_body, onAllow)
        }

        adapter = AlertAdapter(context, onClick)
        adapter.submitList(emptyList())
        binding.recycleView.adapter = adapter
    }
    private fun initViewModel(){
        val networkManager: NetworkManager = NetworkManagerImpl.getInstance()
        val dataBase: DataBase = DataBase.getInstance(requireContext())
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAOAlerts())
        val repository: Repository = RepositoryImpl(networkManager, localDataSource)

        val factory = AlertViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(AlertViewModel::class.java)
        handleResponseState()
    }

    private fun handleResponseState(){
        lifecycleScope.launch {
            viewModel.alertList.collectLatest { response ->
                when(response){
                    is ResponseState.Loading -> { onLoading() }
                    is ResponseState.Success ->{
                        onSuccess()
                        adapter.submitList(response.data)
                        adapter.notifyDataSetChanged()
                    }
                    is ResponseState.Failure ->{ onFailure(response.error.message) }
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