package io.github.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kPermissionsStorage.ReadStoragePermission
import io.github.kPermissionsStorage.WriteStoragePermission
import io.github.kPermissionsStorage.register
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
        WriteStoragePermission.register()
        ReadStoragePermission.register()
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


    val readStoragePermissionState = rememberPermissionState(ReadStoragePermission) { granted ->
        println("read storage granted = $granted")
    }
    LaunchedEffect(Unit) {
        println("read storage permission state initialized: ${readStoragePermissionState.status}")
    }
    LaunchedEffect(readStoragePermissionState.status) {
        println("read storage permission status changed: ${readStoragePermissionState.status}")
    }


    val writeStoragePermissionState = rememberPermissionState(WriteStoragePermission) { granted ->
        println("write storage granted = $granted")
    }
    LaunchedEffect(Unit) {
        println("write storage permission state initialized: ${writeStoragePermissionState.status}")
    }
    LaunchedEffect(readStoragePermissionState.status) {
        println("write storage permission status changed: ${writeStoragePermissionState.status}")
    }



    MaterialTheme {
        LazyColumn(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
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

                Text("Camera Permission Status: ${cameraPermissionState.status}")


                Button(onClick = {
                    println("Button clicked, requesting write storage permission with status: ${writeStoragePermissionState.status}")
                    when (writeStoragePermissionState.status) {

                        PermissionStatus.Denied -> {
                            writeStoragePermissionState.launchPermissionRequest()
                        }

                        PermissionStatus.DeniedPermanently -> {
                            writeStoragePermissionState.openAppSettings()
                        }

                        else -> {
                            println("write storage permission is ${writeStoragePermissionState.status}")
                        }
                    }
                }) {
                    Text("Request Write storage Permission")
                }

                Text("Write storage Permission Status: ${writeStoragePermissionState.status}")


                Button(onClick = {
                    println("Button clicked, requesting read storage permission with status: ${readStoragePermissionState.status}")
                    when (readStoragePermissionState.status) {

                        PermissionStatus.Denied -> {
                            readStoragePermissionState.launchPermissionRequest()
                        }

                        PermissionStatus.DeniedPermanently -> {
                            readStoragePermissionState.openAppSettings()
                        }

                        else -> {
                            println("read storage permission is ${readStoragePermissionState.status}")
                        }
                    }
                }) {
                    Text("Request Read storage Permission")
                }

                Text("Read storage Permission Status: ${readStoragePermissionState.status}")

            }
        }
    }
}