package io.github.kPermissions_api

import android.app.Activity
import java.lang.ref.WeakReference

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
    actual fun isServiceAvailable(): Boolean
    actual suspend fun refreshStatus(): PermissionStatus
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