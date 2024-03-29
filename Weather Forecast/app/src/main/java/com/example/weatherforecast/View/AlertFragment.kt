package com.example.weatherforecast.View

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.LocalDataSource.DataBase
import com.example.weatherforecast.LocalDataSource.LocalDataSourceImpl
import com.example.weatherforecast.Model.AppSettings
import com.example.weatherforecast.Model.NotificationData
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.AlertUtil.AlertManagement
import com.example.weatherforecast.R
import com.example.weatherforecast.RemoteDataSource.RemoteDataSource
import com.example.weatherforecast.RemoteDataSource.RemoteDataSourceImpl
import com.example.weatherforecast.Repository.Repository
import com.example.weatherforecast.Repository.RepositoryImpl
import com.example.weatherforecast.ViewModel.NotificationViewModel
import com.example.weatherforecast.ViewModel.NotificationViewModelFactory
import com.example.weatherforecast.databinding.FragmentAlertBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlertFragment : Fragment() {
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmCalendar: Calendar
    lateinit var viewModel: NotificationViewModel

    var latitude: String = ""
    var longitude: String = ""
    var notificationType: String = Context.NOTIFICATION_SERVICE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDataOnView(requireContext())
        initViewModel()

        if(arguments != null && requireArguments().containsKey("locationMethod")) {
            latitude = requireArguments().getString("latitude", "0")
            longitude = requireArguments().getString("longitude", "0")
            binding.location.text = requireArguments().getString("locationMethod", "Current Location")
        }

        binding.apply {
            location.setOnClickListener {
                selectLocation(requireContext(), findNavController())
            }
            date.setOnClickListener {
                selectDate(requireContext(), childFragmentManager)
            }
            time.setOnClickListener {
                selectTime(requireContext(), childFragmentManager)
            }
            notification.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
                val selectedText = selectedRadioButton.text.toString()
                when(selectedText) {
                    getString(R.string.notification_1) -> notificationType = Context.NOTIFICATION_SERVICE
                    else -> notificationType = Context.ALARM_SERVICE
                }
            }
            save.setOnClickListener {
                val notificationData = NotificationData(
                    date.text.toString(),
                    time.text.toString(),
                    latitude.toDouble(),
                    longitude.toDouble(),
                    notificationType)

                val alertManagement = AlertManagement()
                alertManagement.setAlarm(requireContext(),  alarmCalendar, notificationData)
                viewModel.insertNotification(notificationData)
                val args = Bundle().apply {
                    putInt("tabNumber", 1)
                }
                findNavController().navigate(R.id.action_alarmFragment_to_mainFragment, args)
            }
        }
    }

    private fun initViewModel(){
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl.getInstance()
        val dataBase: DataBase = DataBase.getInstance(requireContext())
        val localDataSource = LocalDataSourceImpl(dataBase.getDAOLastWeather(), dataBase.getDAOLocations(), dataBase.getDAONotifications())
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        val factory = NotificationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)
    }

    private fun setDataOnView(context: Context){
        binding.apply {
            latitude = AppSettings.getInstance(context).latitude.toString()
            longitude = AppSettings.getInstance(context).longitude.toString()
            location.text = context.getString(R.string.location_0)

            val calendar = Calendar.getInstance()
            date.text = formatDate(calendar.timeInMillis)
            time.text = formatTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

            notificationType = Context.NOTIFICATION_SERVICE
            notification1.isChecked = true

            alarmCalendar = Calendar.getInstance().apply {
                timeInMillis = calendar.timeInMillis
                set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }
        }
    }

    private fun formatDate(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return formatter.format(Date(dateInMillis))
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun selectLocation(context: Context, navController: NavController) {
        val options = arrayOf(context.getString(R.string.location_0),
            context.getString(R.string.location_2),
            context.getString(R.string.location_3)
        )
        val location = MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.dialog_location))
            .setItems(options) { _, which ->
                val args = Bundle().apply {
                    putSerializable("Screen", Screen.ALARM)
                }
                when (which) {
                    0 -> {
                        latitude = AppSettings.getInstance(context).latitude.toString()
                        longitude = AppSettings.getInstance(context).longitude.toString()
                        binding.location.text = "Current Location"
                        true
                    }
                    1 -> {
                        navController.navigate(R.id.action_alarmFragment_to_mapFragment, args)
                        true
                    }
                    2 -> {
                        navController.navigate(R.id.action_alarmFragment_to_savedLocationsFragment, args)
                        true
                    }
                    else -> false
                }
            }
        location.show()
    }

    private fun selectDate(context: Context, fragmentManager: FragmentManager) {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(Calendar.getInstance().timeInMillis)
            .setTitleText(context.getString(R.string.dialog_date))
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            alarmCalendar.timeInMillis = datePicker.selection!!
            binding.date.text = formatDate(datePicker.selection!!)
        }
        datePicker.show(fragmentManager, "date")
    }

    private fun selectTime(context: Context, fragmentManager: FragmentManager) {
        val calendar = Calendar.getInstance()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText(context.getString(R.string.dialog_time))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            alarmCalendar.apply {
                set(Calendar.HOUR_OF_DAY, timePicker.hour)
                set(Calendar.MINUTE, timePicker.minute)
                set(Calendar.SECOND, 0)
            }
            binding.time.text = formatTime(timePicker.hour, timePicker.minute)
        }
        timePicker.show(fragmentManager, "time")

    }

}