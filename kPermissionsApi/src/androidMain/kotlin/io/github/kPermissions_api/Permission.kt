package io.github.kPermissions_api

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
    actual fun isServiceAvailable(): Boolean
}

fun Permission.refreshStatus(): PermissionStatus {
    val sdkInt = Build.VERSION.SDK_INT

    if (androidPermissionName == null ||
        (minSdk != null && sdkInt < minSdk!!) ||
        (maxSdk != null && sdkInt > maxSdk!!)
    ) {
        return PermissionStatus.Granted
    }

    val activity = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable

    val isGranted = ContextCompat.checkSelfPermission(
        activity,
        androidPermissionName!!
    ) == PackageManager.PERMISSION_GRANTED

    if (isGranted) return PermissionStatus.Granted

    val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        androidPermissionName!!
    )

    return if (shouldShowRationale) {
        PermissionStatus.Denied
    } else {
        PermissionStatus.DeniedPermanently
    }
}



object AndroidPermission {
    private var activity: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }
    internal fun getActivity(): Activity? {
        return activity?.get()
    }
}