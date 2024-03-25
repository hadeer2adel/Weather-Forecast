package com.example.weatherforecast.NotificationUtil

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.weatherforecast.R

class NotificationCancelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        NotificationMaker.ringtoneSound.stop()

        val notificationId = intent?.getStringExtra("notificationId")!!.toInt()
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}

