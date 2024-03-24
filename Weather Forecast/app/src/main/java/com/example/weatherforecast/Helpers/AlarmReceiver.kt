package com.example.weatherforecast.Helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import com.example.weatherforecast.R

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null || intent == null)
            return

        val weatherDescription = intent.getStringExtra("weatherDescription")

        val notificationPermission = NotificationPermission(context)
        notificationPermission.sendNotification("tttttttttttttttttttttttt")
        //weatherDescription?.let { notificationPermission.sendNotification(it) }
    }
}