package io.github.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kPermissionsStorage.ReadStoragePermission
import io.github.kPermissionsStorage.WriteStoragePermission
import io.github.kPermissionsStorage.register
import io.github.kpermissionsCamera.CameraPermission
import io.github.kpermissionsCamera.register
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.rememberPermissionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    registerAllPermissions()

    val permissions = rememberPermissions(
        CameraPermission,
        WriteStoragePermission,
        ReadStoragePermission,
    )

    MaterialTheme {
        LazyColumn(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(permissions.size) { index ->
                val item = permissions[index]

                Button(onClick = item.onRequest) {
                    Text("Request ${item.name} Permission")
                }

                Text("${item.name} Permission Status: ${item.state.status}")
            }
        }
    }
}


@Composable
fun rememberPermissions(vararg permissions: Permission): List<PermissionItem> {
    return permissions.map { permission ->
        val state = rememberPermissionState(permission) { granted ->
            println("${permission.name} granted = $granted")
        }

        val onRequest: () -> Unit = {
            when (state.status) {
                PermissionStatus.Denied -> state.launchPermissionRequest()
                PermissionStatus.DeniedPermanently -> state.openAppSettings()
                else -> println("${permission.name} already: ${state.status}")
            }
        }

        PermissionItem(
            name = permission.name,
            permission = permission,
            state = state,
            onRequest = onRequest
        )
    }
}

@Composable
fun registerAllPermissions() {
    remember {
        CameraPermission.register()
        WriteStoragePermission.register()
        ReadStoragePermission.register()
        true
    }
}