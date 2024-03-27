package com.example.weatherforecast.View

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.MenuRes
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.LocalDataSource.LocalDataSource
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.R
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.NotificationViewModel
import com.example.weatherforecast.ViewModel.NotificationViewModelFactory
import com.example.weatherforecast.ViewModel.SettingViewModel
import com.example.weatherforecast.ViewModel.SettingViewModelFactory
import com.example.weatherforecast.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var appSettings: AppSettings
    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appSettings = AppSettings.getInstance(requireContext())

        if(requireArguments().containsKey("locationMethod")) {
            appSettings.locationMethod = requireArguments().getString("locationMethod", "gps")
            appSettings.latitude = requireArguments().getString("latitude", "0").toDouble()
            appSettings.longitude = requireArguments().getString("longitude", "0").toDouble()
        }

        setDataOnView()
        initViewModel()

        setUpNotificationOptions()
        setUpLanguageOptions(view)
        setUpTemperatureOptions(view)
        setUpWindOptions(view)

        binding.location.setOnClickListener { v ->
            binding.location.setImageResource(R.drawable.ic_arrow_down)
            showMenu(v, R.menu.location_options)
        }

        binding.rvLocation.setOnClickListener {
            viewModel.deleteAllLocations()
        }

        binding.rvAlarm.setOnClickListener {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            viewModel.deleteAllNotifications(alarmManager)
        }

    }

    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val localDataSource: LocalDataSource = LocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = SettingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)
    }
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PopupMenu(ContextThemeWrapper(context, R.style.PopupMenuStyle), v)
        } else {
            PopupMenu(context, v)
        }
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            val args = Bundle().apply {
                putSerializable("Screen", Screen.SETTINGS)
            }
            when (menuItem.itemId) {
                R.id.gps -> {
                    findNavController().navigate(R.id.action_settingFragment_to_GPS, args)
                    true
                }
                R.id.map -> {
                    findNavController().navigate(R.id.action_settingFragment_to_mapFragment, args)
                    true
                }
                R.id.saved -> {
                    findNavController().navigate(R.id.action_settingFragment_to_savedLocationsFragment, args)
                    true
                }
                else -> false
            }
        }
        popup.setOnDismissListener {
            binding.location.setImageResource(R.drawable.ic_arrow)
        }
        popup.show()
    }
    private fun setDataOnView(){
        binding.apply {
            if (appSettings.notification)
                notification.isChecked = true

            if (appSettings.language.equals("en"))
                language1.isChecked = true
            else
                language2.isChecked = true

            if (appSettings.temperatureUnit.equals("K")){
                tUnit1.isChecked = true
                wUnit1.isChecked = true
            }
            else if (appSettings.temperatureUnit.equals("C")){
                tUnit2.isChecked = true
                wUnit1.isChecked = true
            }
            else{
                tUnit3.isChecked = true
                wUnit2.isChecked = true
            }

            if (appSettings.locationMethod.equals("saved")){
                locationMethod.text = getString(R.string.location_3)
            }
            else if (appSettings.locationMethod.equals("map")){
                locationMethod.text = getString(R.string.location_2)
            }
            else{
                locationMethod.text = getString(R.string.location_1)
            }
        }
    }
    private fun setUpNotificationOptions(){
        binding.notification.setOnCheckedChangeListener { buttonView, isChecked ->
                appSettings.notification = isChecked
        }
    }
    private fun setUpLanguageOptions(view: View){
        binding.language.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            when(selectedText) {
                getString(R.string.language_2) -> appSettings.language = "ar"
                else -> appSettings.language = "en"
            }
        }
    }
    private fun setUpTemperatureOptions(view: View){
        binding.temperature.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            when(selectedText) {
                getString(R.string.t_unit_3) -> { binding.wUnit2.isChecked = true; appSettings.temperatureUnit = "F" }
                getString(R.string.t_unit_2) -> { binding.wUnit1.isChecked = true; appSettings.temperatureUnit = "C" }
                else -> { binding.wUnit1.isChecked = true; appSettings.temperatureUnit = "K" }
            }
        }
    }
    private fun setUpWindOptions(view: View){
        binding.wind.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            when(selectedText) {
                getString(R.string.w_unit_2) -> { binding.tUnit3.isChecked = true; appSettings.windUnit = "mi/hr" }
                else -> {
                    if(!binding.tUnit1.isChecked)
                        binding.tUnit2.isChecked = true
                    appSettings.windUnit = "m/s"
                }
            }
        }
    }

}