package io.github.kpermissions.handler

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random


fun permissionRequest(permissions: Array<String>, onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val activity = context as Activity

    val ungrantedPermissions = permissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (ungrantedPermissions.isEmpty()) {
        // All permissions are already granted
        onPermissionResult(true)
        return
    }

    val shouldShowRationale = ungrantedPermissions.any {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
    }

    val requestCode = Random.nextInt(1000, 9999)

    if (shouldShowRationale) {
        // Show rationale and then request permissions
        ActivityCompat.requestPermissions(
            activity,
            ungrantedPermissions.toTypedArray(),
            requestCode
        )
    } else {
        // Request permissions directly

//            ActivityCompat.requestPermissions(activity, ungrantedPermissions.toTypedArray(), requestCode)
        if (PermissionHandler.openSetting) {
            openAppSettings()
        }
    }

    PermissionHandler.onPermissionResult = { _, grantResults ->
        val allGranted =
            grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        onPermissionResult(allGranted)
    }
}