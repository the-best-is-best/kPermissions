package io.github.kpermissions.handler

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun openAppSettings() {
    val context = PermissionHandler.getAppContext()
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", context.packageName, null)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Handle case where the settings activity is not found
        e.printStackTrace()
        println("open setting error ${e.localizedMessage}")
    }
}