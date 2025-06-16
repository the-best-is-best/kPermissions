package io.github.kPermissions_api

import android.content.pm.PackageManager
import android.os.Build

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
    actual suspend fun isServiceAvailable(): Boolean
}


suspend fun Permission.refreshState(): PermissionStatus {
    val sdkInt = Build.VERSION.SDK_INT

    // If permission name is null or not applicable for this SDK version, consider it granted
    if (androidPermissionName == null ||
        (minSdk != null && sdkInt < minSdk!!) ||
        (maxSdk != null && sdkInt > maxSdk!!)
    ) {
        return PermissionStatus.Granted
    }

    if (!isServiceAvailable()) return PermissionStatus.Unavailable


    return PermissionStatus.Denied
}


fun Permission.isDeclaredInManifest(): Boolean {
    val androidPermission = androidPermissionName ?: return false
    val context = AppContextProvider.appContext
    return try {
        val info = context.packageManager?.getPackageInfo(
            context.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val permissions = info?.requestedPermissions?.toList() ?: emptyList()
        permissions.contains(androidPermission)
    } catch (e: Exception) {
        false
    }
}
