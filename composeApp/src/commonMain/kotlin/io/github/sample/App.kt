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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissionlocationwheninuseext.openPrivacySettings
import io.github.kpermissionnotification.NotificationPermission
import io.github.kpermissionsCamera.CameraPermission
import io.github.kpermissionsCore.rememberMultiplePermissionsState
import io.github.kpermissionsCore.rememberPermissionState
import io.github.kpermissionsbluetooth.BluetoothPermission
import io.github.kpermissionsbluetooth.bluetoothStateFlow
import io.github.kpermissionscmpbluetooth.openBluetoothSettingsCMP
import io.github.kpermissionslocationAlways.LocationAlwaysPermission
import io.github.kpermissionslocationChecker.locationServiceEnabledFlow
import io.github.kpermissionslocationWhenInUse.LocationInUsePermission
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class PermissionScreen { Single, Multi }

@Composable
@Preview
fun App() {
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
        ReadVideoPermission,
        LocationInUsePermission,
        LocationAlwaysPermission,
        NotificationPermission,
        BluetoothPermission
    )


    val unavailablePermissions = permissions.filterNot { it.isServiceAvailable() }.map { it.name }

    var showUnavailableDialog by remember { mutableStateOf(false) }
    var clickedUnavailablePermission by remember { mutableStateOf<String?>(null) }


    val isLocationEnabled by locationServiceEnabledFlow.collectAsState(initial = false)
    val isBluetoothOn by bluetoothStateFlow().collectAsState(initial = false)

    Column(

        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        permissions.forEach { permission ->
            val state = rememberPermissionState(permission) { granted ->
                println("${permission.name} granted = $granted")
            }
            if (state.permission is LocationInUsePermission || state.permission is LocationAlwaysPermission) {
                LaunchedEffect(isLocationEnabled) {
                    LocationAlwaysPermission.isServiceAvailable()
                    LocationInUsePermission.isServiceAvailable()
                    state.checkPermissionStatus()
                }
            }
            if (state.permission is BluetoothPermission) {
                LaunchedEffect(isBluetoothOn) {
                    BluetoothPermission.isServiceAvailable()
                    state.checkPermissionStatus()
                }
            }

            val onRequest: () -> Unit = {
                when (state.status) {
                    PermissionStatus.Denied -> state.launchPermissionRequest()
                    PermissionStatus.DeniedPermanently -> state.openAppSettings()
                    PermissionStatus.Unavailable -> {
                        try {
                            when (permission) {
                                is LocationInUsePermission, is LocationAlwaysPermission -> {
                                    LocationInUsePermission.openPrivacySettings()
                                }

                                is BluetoothPermission -> {
                                    BluetoothPermission.openBluetoothSettingsCMP()
                                }

                                else -> {
                                    clickedUnavailablePermission = permission.name
                                    showUnavailableDialog = true
                                }
                            }
                        } catch (e: UnsupportedOperationException) {
                            clickedUnavailablePermission = permission.name
                            showUnavailableDialog = true
                        }

                    }
                    else -> println("${permission.name} already: ${state.status}")
                }
            }

            Button(onClick = onRequest) {
                Text("Request ${permission.name} Permission")
            }

            Text("${permission.name} Permission Status: ${state.status}")

        }

        if (unavailablePermissions.isNotEmpty()) {
            Button(onClick = { showUnavailableDialog = true }) {
                Text("Show All Unavailable Permissions")
            }
        }
    }

    if (showUnavailableDialog) {
        UnavailablePermissionsDialog(
            unavailable = listOfNotNull(clickedUnavailablePermission).ifEmpty { unavailablePermissions },
            onDismiss = {
                showUnavailableDialog = false
                clickedUnavailablePermission = null
            }
        )
    }
}


@Composable
fun MultiPermissionTestScreen() {
    val requiredPermissions = listOf<Permission>(
        CameraPermission,
        GalleryPermission
    )

    val availablePermissions = remember {
        requiredPermissions.filter { it.isServiceAvailable() }
    }

    val unavailablePermissions = remember {
        requiredPermissions.filterNot { it.isServiceAvailable() }.map { it.name }
    }

    var showUnavailableDialog by remember { mutableStateOf(false) }

    val states = rememberMultiplePermissionsState(
        permissions = availablePermissions,
        onPermissionsResult = { granted ->
            println("All permissions granted? $granted")
        }
    )

    var allGranted by remember { mutableStateOf(false) }

    LaunchedEffect(states.map { it.status }) {
        allGranted = states.all { it.status == PermissionStatus.Granted }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            if (unavailablePermissions.isNotEmpty()) {
                showUnavailableDialog = true
                return@Button
            }

            states.forEach { state ->

            when (state.status) {
                    PermissionStatus.Denied -> state.launchPermissionRequest()
                    PermissionStatus.DeniedPermanently -> state.openAppSettings()
                    else -> Unit
                }
            }

        }) {
            Text("Request All Permissions")
        }


        Text("All Permissions Granted: $allGranted")

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(states) { state ->
                Text("${state.permission.name}: ${state.status}")
            }
        }
    }

    if (showUnavailableDialog) {
        UnavailablePermissionsDialog(
            unavailable = unavailablePermissions,
            onDismiss = { showUnavailableDialog = false }
        )
    }
}
