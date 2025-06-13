package io.github.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kPermissionsAudio.ReadAudioPermission
import io.github.kPermissionsGallery.GalleryPermission
import io.github.kPermissionsStorage.ReadStoragePermission
import io.github.kPermissionsStorage.WriteStoragePermission
import io.github.kPermissionsVideo.ReadVideoPermission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissionsCamera.CameraPermission
import io.github.kpermissionsCore.rememberMultiplePermissionsState
import io.github.kpermissionsCore.rememberPermissionState
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class PermissionScreen { Single, Multi }

@Composable
@Preview
fun App() {
    remember {
        true
    }
    var selectedScreen by remember { mutableStateOf<PermissionScreen?>(null) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (selectedScreen) {
                null -> {
                    Button(onClick = { selectedScreen = PermissionScreen.Single }) {
                        Text("Test Single Permissions")
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = { selectedScreen = PermissionScreen.Multi }) {
                        Text("Test Multi Permissions")
                    }
                }

                PermissionScreen.Single -> {
                    SinglePermissionsScreen()
                }

                PermissionScreen.Multi -> {
                    MultiPermissionTestScreen()
                }
            }
        }
    }
}

@Composable
fun SinglePermissionsScreen() {
    val permissions = listOf(
        CameraPermission,
        WriteStoragePermission,
        ReadStoragePermission,
        GalleryPermission,
        ReadAudioPermission,
        ReadVideoPermission
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        permissions.forEach { permission ->
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

            Button(onClick = onRequest) {
                Text("Request ${permission.name} Permission")
            }

            Text("${permission.name} Permission Status: ${state.status}")
        }
    }
}



@Composable
fun MultiPermissionTestScreen() {

    val requiredPermissions = listOf(
        CameraPermission,
        ReadStoragePermission,
        WriteStoragePermission,
        GalleryPermission
    )

    var allGranted by remember { mutableStateOf(false) }

    val states = rememberMultiplePermissionsState(
        permissions = requiredPermissions,
        onPermissionsResult = { granted ->
            allGranted = granted
            println("All permissions granted? $granted")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            states.forEach { state ->
                when (state.status) {
                    PermissionStatus.Denied -> state.launchPermissionRequest()
                    PermissionStatus.DeniedPermanently -> state.openAppSettings()
                    else -> Unit
                }
            }

            allGranted = states.all { it.status == PermissionStatus.Granted }

        }) {
            Text("Request All Permissions")
        }
        Text("All Permissions Granted: $allGranted")

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(states.size) { index ->
                val state = states[index]
                Text("${requiredPermissions[index].name}: ${state.status}")
            }
        }
    }
}
