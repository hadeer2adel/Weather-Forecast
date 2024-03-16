package com.example.weatherforecast.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.weatherforecast.Helpers.getFromSharedPreferences
import com.example.weatherforecast.Helpers.saveOnSharedPreferences
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private var notificationText = "true"
    private var languageText = "eg"
    private var temperatureUnit = "K"
    private var windUnit = "m/s"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setDataOnView()
        setUpNotificationOptions()
        setUpLanguageOptions(view)
        setUpTemperatureOptions(view)
        setUpWindOptions(view)
        //setUpRemoveBtn()
    }
    private fun getData(){
        notificationText =  getFromSharedPreferences(requireContext(), "notification", notificationText)
        languageText =  getFromSharedPreferences(requireContext(), "language", languageText)
        temperatureUnit = getFromSharedPreferences(requireContext(), "temperatureUnit", temperatureUnit)
        windUnit =  getFromSharedPreferences(requireContext(), "windUnit", windUnit)
    }
    private fun setDataOnView(){

        binding.apply {
            if (notificationText.equals("true"))
                notification.isChecked = true

            if (languageText.equals("eg"))
                language1.isChecked = true
            else
                language2.isChecked = true

            if (temperatureUnit.equals("K")){
                tUnit1.isChecked = true
                wUnit1.isChecked = true
            }
            else if (temperatureUnit.equals("C")){
                tUnit2.isChecked = true
                wUnit1.isChecked = true
            }
            else{
                tUnit3.isChecked = true
                wUnit2.isChecked = true
            }
        }
    }
    private fun setUpNotificationOptions(){
        binding.notification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                saveOnSharedPreferences(requireContext(), "notification", "true")
            else
                saveOnSharedPreferences(requireContext(), "notification", "false")
        }
    }
    private fun setUpLanguageOptions(view: View){
        binding.language.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            if (selectedText.equals(getString(R.string.language_2)))
                saveOnSharedPreferences(requireContext(), "language", "ar")
            else
                saveOnSharedPreferences(requireContext(), "language", "eg")
        }
    }
    private fun setUpTemperatureOptions(view: View){
        binding.temperature.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            if (selectedText.equals(getString(R.string.t_unit_3))) {
                binding.wUnit2.isChecked = true
                saveOnSharedPreferences(requireContext(), "temperatureUnit", "F")
            }
            else if (selectedText.equals(getString(R.string.t_unit_2))) {
                binding.wUnit1.isChecked = true
                saveOnSharedPreferences(requireContext(), "temperatureUnit", "C")
            }
            else {
                binding.wUnit1.isChecked = true
                saveOnSharedPreferences(requireContext(), "temperatureUnit", "K")
            }
        }
    }
    private fun setUpWindOptions(view: View){
        binding.wind.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val selectedText = selectedRadioButton.text.toString()
            if (selectedText.equals(getString(R.string.w_unit_2))) {
                binding.tUnit3.isChecked = true
                saveOnSharedPreferences(requireContext(), "windUnit", "mi/hr")
            } else {
                binding.tUnit2.isChecked = true
                saveOnSharedPreferences(requireContext(), "windUnit", "m/s")
            }
        }
    }

}