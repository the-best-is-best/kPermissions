package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest

fun requestMicrophonePermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.RECORD_AUDIO

    permissionRequest(arrayOf(permission), onPermissionResult)

}