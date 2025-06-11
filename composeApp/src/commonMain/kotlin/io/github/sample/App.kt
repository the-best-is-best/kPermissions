package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kpermissionsCamera.CameraPermission
import io.github.kpermissionsCamera.register
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.rememberPermissionState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    remember {
        CameraPermission.register()
        true
    }
    val cameraPermissionState = rememberPermissionState(CameraPermission) { granted ->
        println("Camera granted = $granted")
    }
    LaunchedEffect(Unit) {
        println("Camera permission state initialized: ${cameraPermissionState.status}")
    }
    LaunchedEffect(cameraPermissionState.status) {
        println("Camera permission status changed: ${cameraPermissionState.status}")
    }
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                println("Button clicked, requesting camera permission with status: ${cameraPermissionState.status}")
                when (cameraPermissionState.status) {

                    PermissionStatus.Denied -> {
                        cameraPermissionState.launchPermissionRequest()
                    }

                    PermissionStatus.DeniedPermanently -> {
                        cameraPermissionState.openAppSettings()
                    }

                    else -> {
                        println("Camera permission is ${cameraPermissionState.status}")
                    }
                }
            }) {
                Text("Request Camera Permission")
            }

        }
    }
}