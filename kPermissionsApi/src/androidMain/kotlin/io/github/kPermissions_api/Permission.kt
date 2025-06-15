package io.github.kPermissions_api

import android.app.Activity
import android.content.pm.PackageManager
import java.lang.ref.WeakReference

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
    actual fun isServiceAvailable(): Boolean
}
//
//fun Permission.checkPermissionStatus(): PermissionStatus {
//    val sdkInt = Build.VERSION.SDK_INT
//
//    // If permission name is null or not applicable for this SDK version, consider it granted
//    if (androidPermissionName == null ||
//        (minSdk != null && sdkInt < minSdk!!) ||
//        (maxSdk != null && sdkInt > maxSdk!!)
//    ) {
//        return PermissionStatus.Granted
//    }
//
//    val activity = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable
//
//    // Check if permission is declared in AndroidManifest.xml
//    if (!isDeclaredInManifest()) return PermissionStatus.NotDeclared
//
//    // Check if permission is already granted
//    val isGranted = ContextCompat.checkSelfPermission(activity, androidPermissionName!!) == PackageManager.PERMISSION_GRANTED
//    if (isGranted) return PermissionStatus.Granted
//
//    // Check if we should show rationale (means user denied but didn't select "Don't ask again")
//    val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName!!)
//
//    // SharedPreferences to keep track if permission was requested before
//    val sharedPrefs = activity.getSharedPreferences("kPermissions_cmp", Context.MODE_PRIVATE)
//    val alreadyRequested = sharedPrefs.getBoolean("requested_$name", false)
//
//    return when {
//        // User denied permission but can be asked again
//        shouldShowRationale -> PermissionStatus.Denied
//
//        // Permission was never requested before, so treat as denied (first time request)
//        !alreadyRequested -> PermissionStatus.Denied
//
//        // Otherwise, user denied permission permanently (selected "Don't ask again")
//        else -> PermissionStatus.Denied
//    }
//}


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