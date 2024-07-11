package io.github.kpermissions.handler

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

suspend fun permissionRequest(permissions: Array<String>, onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val activity = context as Activity

    // Launch a coroutine to handle permission request
    CoroutineScope(Dispatchers.Main).launch {
        // Execute on the main thread
        val result = waitPermissionRequest(permissions, activity)

        // This code block will execute after waitPermissionRequest completes
        if (result) {
            // Permissions granted
            onPermissionResult(true)
        } else {
            // Permissions not fully granted
            if (PermissionHandler.openSetting) {
                openAppSettings()
            }
            onPermissionResult(false)
        }
    }
}

private suspend fun waitPermissionRequest(permissions: Array<String>, activity: Activity): Boolean {
    return withContext(Dispatchers.IO) {
        // Switch to IO dispatcher for permission checks
        val ungrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (ungrantedPermissions.isEmpty()) {
            // All permissions are already granted
            true
        } else {
            // Request permissions
            val shouldShowRationale = ungrantedPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }

            val requestCode = Random.nextInt(1000, 9999)

            // Request permissions on the main thread
            ActivityCompat.requestPermissions(
                activity,
                ungrantedPermissions.toTypedArray(),
                requestCode
            )

            false // Permissions are not fully granted yet
        }
    }
}
