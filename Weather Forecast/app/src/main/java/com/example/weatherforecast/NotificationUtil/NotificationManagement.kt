package com.example.weatherforecast.NotificationUtil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.weatherforecast.Model.NotificationData
import java.util.Calendar

class NotificationManagement {

     fun setAlarm(context: Context, requestId: String, time: Calendar, notificationType: String, latitude: String, longitude: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("requestId", requestId)
        intent.putExtra("notificationType", notificationType)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            pendingIntent
        )
    }

     fun cancelAlarm(context: Context, notification: NotificationData){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("id", notification.requestId)
        intent.putExtra("notificationType", notification.notificationType)
        intent.putExtra("latitude", notification.latitude.toString())
        intent.putExtra("longitude", notification.longitude.toString())
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }


}