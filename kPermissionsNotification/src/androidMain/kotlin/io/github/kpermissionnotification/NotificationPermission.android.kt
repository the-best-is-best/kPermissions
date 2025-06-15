package io.github.kpermissionnotification

import android.os.Build
import io.github.kPermissions_api.Permission

actual object NotificationPermission : Permission {
    override val name: String
        get() = "notification"

    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.POST_NOTIFICATIONS
        } else {
            null
        }

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null
    override val minSdk: Int?
        get() = _minSdk
    override val maxSdk: Int?
        get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override fun isServiceAvailable(): Boolean {
        return true

    }


}