package com.example.weatherforecast.Helpers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.View.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationPermission(val context: Context) {
    private val CHANNEL_ID = "weather_notification"
    private val notificationId = 101

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weather alarm notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Show weather forecast details"
            val manager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(
        smallDescription: String,
        largeDescription: String,
        image: Bitmap
    ) {
        setNotificationChannel()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Weather Alarm")
            .setContentText(smallDescription)
            .setLargeIcon(image)
            .setStyle(NotificationCompat.BigTextStyle().bigText(largeDescription))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = NotificationManagerCompat.from(context)
        manager.notify(notificationId, notification)
    }

    fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Allow") { _, _ -> enableNotificationServices() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun enableNotificationServices() {
        Toast.makeText(context, "Turn on notification", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivity(context, intent, null)
    }
}