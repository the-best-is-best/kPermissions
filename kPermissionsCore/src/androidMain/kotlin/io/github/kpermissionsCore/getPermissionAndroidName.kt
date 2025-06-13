package io.github.kpermissionsCore

import android.Manifest
import android.os.Build
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionType

fun Permission.getAndroidName(): String? {
    return when (this.type) {
        PermissionType.Camera -> Manifest.permission.CAMERA
        PermissionType.Gallery -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else null
        PermissionType.ReadStorage ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_EXTERNAL_STORAGE else null

        PermissionType.WriteStorage ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) Manifest.permission.WRITE_EXTERNAL_STORAGE else null

        PermissionType.ReadVideo ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else null

        PermissionType.ReadAudio ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else null
    }
}