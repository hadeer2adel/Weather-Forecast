package com.example.weatherforecast.Helpers

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.weatherforecast.R

fun showDialog(context: Context, titleId: Int, bodyId: Int, onAllow: ()->Unit) {
    MaterialAlertDialogBuilder(
        context,
        com.google.android.material.R.style.MaterialAlertDialog_Material3
    )
        .setTitle(context.getString(titleId))
        .setMessage(context.getString(bodyId))
        .setPositiveButton(context.getString(R.string.sure)) { _, _ -> onAllow() }
        .setNegativeButton(context.getString(R.string.cancel), null)
        .show()
}

fun showNetworkDialog(context: Context) {
    MaterialAlertDialogBuilder(
        context,
        com.google.android.material.R.style.MaterialAlertDialog_Material3
    )
        .setTitle(context.getString(R.string.connection_title))
        .setMessage(context.getString(R.string.connection_body))
        .setNegativeButton(context.getString(R.string.cancel), null)
        .show()
}