package com.example.weatherforecast.Helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.weatherforecast.Model.Screen
import com.example.weatherforecast.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class NotificationMaker (val context: Context){

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    fun selectLocation(navController: NavController) {
        val options = arrayOf(context.getString(R.string.location_1),
            context.getString(R.string.location_2),
            context.getString(R.string.location_3)
        )
        val location = MaterialAlertDialogBuilder(context)
            .setTitle("Select Location")
            .setItems(options) { _, which ->
                val args = Bundle().apply {
                    putSerializable("Screen", Screen.ALARM)
                }
                when (which) {
                    0 -> {
                        Log.i("TAG", "selectLocation: ")
                        navController.navigate(R.id.action_mainFragment_to_GPS, args)
                        true
                    }
                    1 -> {
                        navController.navigate(R.id.action_mainFragment_to_mapFragment, args)
                        true
                    }
                    2 -> {
                        navController.navigate(R.id.action_mainFragment_to_savedLocationsFragment, args)
                        true
                    }
                    else -> false
                }
            }
        location.show()
    }

    fun selectAlarm(fragmentManager: FragmentManager, latitude: String, longitude: String) {
        val calendar = Calendar.getInstance()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Select Time")
            .build()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            timePicker.show(fragmentManager, "time")

            timePicker.addOnPositiveButtonClickListener {
                val date = Calendar.getInstance().apply {
                    timeInMillis = datePicker.selection!!
                    set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    set(Calendar.MINUTE, timePicker.minute)
                    set(Calendar.SECOND, 0)
                }
                setAlarm(date, latitude, longitude)
            }
        }
        datePicker.show(fragmentManager, "date")
    }

    private fun setAlarm(time: Calendar, latitude: String, longitude: String){
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            pendingIntent
        )
    }
    private fun cancelAlarm(){
        alarmManager.cancel(pendingIntent)
    }

}