package com.example.weatherforecast.AlertUtil

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlertCancelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        AlertMaker.ringtoneSound.stop()

        val notificationId = intent?.getIntExtra("notificationId", 101)!!
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}

