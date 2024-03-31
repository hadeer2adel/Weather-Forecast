package com.example.weatherforecast.AlertUtil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.weatherforecast.Helpers.getTimeInMillis
import com.example.weatherforecast.Model.AlertData
import java.util.Calendar

class AlertManagement {

     fun setAlarm(context: Context, time: Calendar, alert: AlertData) {
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, AlertReceiver::class.java)

         intent.putExtra("date", alert.date)
         intent.putExtra("time", alert.time)
         intent.putExtra("notificationType", alert.notificationType)
         intent.putExtra("latitude", alert.latitude.toString())
         intent.putExtra("longitude", alert.longitude.toString())

         val requestCode = getTimeInMillis(alert.date, alert.time)
         val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

         alarmManager.setExact(
             AlarmManager.RTC_WAKEUP,
             time.timeInMillis,
             pendingIntent
         )
     }

     fun cancelAlarm(context: Context, alert: AlertData) {
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, AlertReceiver::class.java)

         intent.putExtra("date", alert.date)
         intent.putExtra("time", alert.time)
         intent.putExtra("notificationType", alert.notificationType)
         intent.putExtra("latitude", alert.latitude.toString())
         intent.putExtra("longitude", alert.longitude.toString())

         val requestCode = getTimeInMillis(alert.date, alert.time)
         val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

         alarmManager.cancel(pendingIntent)
     }


}