package io.github.kpermissionsCamera

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = Manifest.permission.CAMERA,
        onResult = onResult,
        prefsKey = "camera_permission_state"

    )
}
