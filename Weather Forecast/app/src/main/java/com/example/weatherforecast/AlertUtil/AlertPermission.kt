package com.example.weatherforecast.AlertUtil

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.weatherforecast.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertPermission(val context: Context) {

    fun showDeviceSettingDialog() {
        MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle(context.getString(R.string.notification_permission_title))
            .setMessage(context.getString(R.string.notification_permission_body))
            .setPositiveButton(context.getString(R.string.allow)) { _, _ -> enableAlertServices() }
            .setNegativeButton(context.getString(R.string.cancel), null)
            .show()
    }

    fun showMyAppSettingDialog(navController: NavController) {
        MaterialAlertDialogBuilder(
            context,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle(context.getString(R.string.notification_permission_title))
            .setMessage(context.getString(R.string.notification_permission_body))
            .setPositiveButton(context.getString(R.string.allow)) { _, _ -> navController.navigate(R.id.action_mainFragment_to_settingFragment) }
            .setNegativeButton(context.getString(R.string.cancel), null)
            .show()
    }

    private fun enableAlertServices() {
        Toast.makeText(context, context.getString(R.string.turnon_notification), Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        startActivity(context, intent, null)
    }
}