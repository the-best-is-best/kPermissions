package io.github.kPermissions_api

import android.app.Activity
import android.content.Context
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

fun Permission.checkPermissionStatus(): PermissionStatus {
    val sdkInt = Build.VERSION.SDK_INT

    if (androidPermissionName == null ||
        (minSdk != null && sdkInt < minSdk!!) ||
        (maxSdk != null && sdkInt > maxSdk!!)
    ) {
        return PermissionStatus.Granted
    }

    val activity = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable

    val isDeclaredInManifest = try {
        val info = activity.packageManager.getPackageInfo(
            activity.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val permissions = info.requestedPermissions?.toList() ?: emptyList()
        permissions.contains(androidPermissionName)
    } catch (e: Exception) {
        false
    }

    if (!isDeclaredInManifest) return PermissionStatus.NotDeclared

    val isGranted = ContextCompat.checkSelfPermission(
        activity,
        androidPermissionName!!
    ) == PackageManager.PERMISSION_GRANTED

    if (isGranted) return PermissionStatus.Granted

    val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        androidPermissionName!!
    )

    val sharedPrefs = activity.getSharedPreferences("kPermissions_cmp", Context.MODE_PRIVATE)
    val alreadyRequested = sharedPrefs.getBoolean("requested_$name", false)

    return when {
        shouldShowRationale -> PermissionStatus.Denied // رفض عادي
        !alreadyRequested -> PermissionStatus.Denied // أول مرة، أو لسه مفيش طلب أصلاً
        else -> PermissionStatus.DeniedPermanently // Don’t ask again
    }
}

fun Permission.isDeclaredInManifest(): Boolean {
    val androidPermission = androidPermissionName ?: return false
    val context = AndroidPermission.getActivity() ?: return false
    return try {
        val info = AndroidPermission.getActivity()?.packageManager?.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val permissions = info?.requestedPermissions?.toList() ?: emptyList()
        permissions.contains(androidPermission)
    } catch (e: Exception) {
        false
    }
}


object AndroidPermission {
    private var activity: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }
    fun getActivity(): Activity? {
        return activity?.get()
    }
}