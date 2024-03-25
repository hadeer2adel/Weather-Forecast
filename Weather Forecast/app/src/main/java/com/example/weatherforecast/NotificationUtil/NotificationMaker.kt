package com.example.weatherforecast.NotificationUtil

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.View.MainActivity


class NotificationMaker(val context: Context) {
    companion object {
        lateinit var ringtoneSound: Ringtone
    }
    private val Notification_CHANNEL_ID = "weather_notification"
    private val Alarm_CHANNEL_ID = "weather_alarm"
    private val notificationId = 101

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Notification_CHANNEL_ID,
                "Weather Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Show weather forecast details"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun setAlarmChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Alarm_CHANNEL_ID,
                "Weather Alarm",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Show weather forecast details"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun makeNotification(weatherDescription: String) {
        setNotificationChannel()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, Notification_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Weather Alarm")
            .setContentText(weatherDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(weatherDescription))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(notificationId, notification)
    }

    @SuppressLint("MissingPermission")
    fun makeAlarm(weatherDescription: String) {
        setAlarmChannel()

        val cancelIntent = Intent(context, NotificationCancelReceiver::class.java)
        cancelIntent.putExtra("notificationId", notificationId.toString())
        val cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val fullScreenIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, Alarm_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Weather Alarm")
            .setContentText(weatherDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(weatherDescription))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .addAction(R.drawable.ic_close, "Cancel", cancelPendingIntent)
            .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(notificationId, notification)

        val notificationSound = Uri.parse("android.resource://${context.packageName}/${R.raw.sound}")
        ringtoneSound = RingtoneManager.getRingtone(context.applicationContext, notificationSound)
        ringtoneSound.play()
    }
}