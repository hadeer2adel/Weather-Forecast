package com.example.weatherforecast.View

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var appSettings: AppSettings

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

        binding.location.setOnClickListener { v ->
            binding.location.setImageResource(R.drawable.ic_arrow_down)
            showMenu(v, R.menu.location_options)
        }
        setDataOnView()
        setUpNotificationOptions()
        setUpLanguageOptions(view)
        setUpTemperatureOptions(view)
        setUpWindOptions(view)
        //setUpRemoveBtn()
    }
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PopupMenu(ContextThemeWrapper(context, R.style.PopupMenuStyle), v)
        } else {
            PopupMenu(context, v)
        }
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.gps -> {
                    appSettings.locationMethod = "gps"
                    val navController = findNavController()
                    navController.navigate(R.id.action_settingFragment_to_GPS)
                    true
                }
                R.id.map -> {
                    val navController = findNavController()
                    navController.navigate(R.id.action_settingFragment_to_mapFragment)
                    true
                }
                R.id.saved -> {
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

            if (appSettings.language.equals("eg"))
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
                else -> appSettings.language = "eg"
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