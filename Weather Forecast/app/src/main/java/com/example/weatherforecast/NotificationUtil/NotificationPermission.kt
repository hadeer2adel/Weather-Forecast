package com.example.weatherforecast.NotificationUtil

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.weatherforecast.R
import com.example.weatherforecast.View.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationPermission(val context: Context) {

    fun showDeviceSettingDialog() {
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

    fun showMyAppSettingDialog(navController: NavController) {
        MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Allow") { _, _ -> navController.navigate(R.id.action_mainFragment_to_settingFragment) }
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