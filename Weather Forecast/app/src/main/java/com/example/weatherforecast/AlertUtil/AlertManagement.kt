package com.example.weatherforecast.AlertUtil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.weatherforecast.Model.NotificationData
import java.util.Calendar

class AlertManagement {

     fun setAlarm(context: Context, time: Calendar, notification: NotificationData) {
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, AlertReceiver::class.java)

         intent.putExtra("date", notification.date)
         intent.putExtra("time", notification.time)
         intent.putExtra("notificationType", notification.notificationType)
         intent.putExtra("latitude", notification.latitude.toString())
         intent.putExtra("longitude", notification.longitude.toString())

         val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

         alarmManager.setExact(
             AlarmManager.RTC_WAKEUP,
             time.timeInMillis,
             pendingIntent
         )
     }

     fun cancelAlarm(context: Context, notification: NotificationData) {
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, AlertReceiver::class.java)

         intent.putExtra("date", notification.date)
         intent.putExtra("time", notification.time)
         intent.putExtra("notificationType", notification.notificationType)
         intent.putExtra("latitude", notification.latitude.toString())
         intent.putExtra("longitude", notification.longitude.toString())

         val pendingIntent =
             PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

         alarmManager.cancel(pendingIntent)
     }


}