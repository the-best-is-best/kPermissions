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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    var unavailablePermissions by remember { mutableStateOf<List<String>>(emptyList()) }
    var showUnavailableDialog by remember { mutableStateOf(false) }
    var clickedUnavailablePermission by remember { mutableStateOf<String?>(null) }


    // Check unavailable services at launch
    LaunchedEffect(Unit) {
        val unavailable = permissions.filterNot { it.isServiceAvailable() }.map { it.name }
        unavailablePermissions = unavailable
    }
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 16.dp).verticalScroll(rememberScrollState())
    ) {
        // 🔔 Show permanent location warning if service is OFF


        permissions.forEach { permission ->
            val state = rememberPermissionState(permission)


            // Refresh Bluetooth & Location state
            LaunchedEffect(Unit) {
                launch {
                    locationServiceEnabledFlow.collectLatest { state.refreshStatus() }
                }
                launch {
                    bluetoothStateFlow().collectLatest { state.refreshStatus() }
                }
            }

            val onRequest: () -> Unit = {
                when (state.status) {
                    PermissionStatus.Granted -> println("${permission.name} is already granted.")
                    PermissionStatus.Denied -> state.launchPermissionRequest()
                    PermissionStatus.DeniedPermanently -> state.openAppSettings()
                    PermissionStatus.Unavailable -> {
                        try {
                            when (permission) {
                                is LocationInUsePermission, is LocationAlwaysPermission -> {
                                    scope.launch {
                                        LocationInUsePermission.openPrivacySettings()
                                    }
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
                    PermissionStatus.NotDeclared -> {}
                }
            }

            Button(onClick = onRequest) {
                Text("Request ${permission.name} Permission")
            }

            Text(text = "${permission.name} Permission Status: ${state.status}")
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
    val requiredPermissions = listOf(
        NotificationPermission,
        ReadStoragePermission,
        CameraPermission,
        GalleryPermission
    )

    // حالة الصلاحيات الغير متاحة بسبب الخدمة
    var unavailablePermissions by remember { mutableStateOf<List<String>>(emptyList()) }

    // تشغيل الفحص عند الإنشاء
    LaunchedEffect(Unit) {
        val unavailable = mutableListOf<String>()
        for (p in requiredPermissions) {
            if (!p.isServiceAvailable()) {
                unavailable.add(p.name)
            }
        }
        unavailablePermissions = unavailable
    }

    var showUnavailableDialog by remember { mutableStateOf(false) }
    var shouldTriggerLauncher by remember { mutableStateOf(false) }

    // حالة الصلاحيات المتعددة
    val states = rememberMultiplePermissionsState(
        permissions = requiredPermissions
    )

    // تنفيذ الطلب عند التفعيل
    LaunchedEffect(shouldTriggerLauncher) {
        if (shouldTriggerLauncher) {
            states.launchPermissionsRequest()
            shouldTriggerLauncher = false

            if (states.anyPermissionDeniedPermanently()) {
                states.openAppSettings()
            }
        }
    }

    val allGranted = states.allPermissionsGranted()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            if (unavailablePermissions.isNotEmpty()) {
                showUnavailableDialog = true
            } else {
                shouldTriggerLauncher = true
            }
        }) {
            Text("Request All Permissions")
        }

        Text("All Permissions Granted: $allGranted")

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(states.permissions.size) { index ->
                // تأكد من أن عدد permissions و statuses متساوي
                val perm = states.permissions.getOrNull(index)
                val status = states.statuses.getOrNull(index)
                if (perm != null && status != null) {
                    Text("${perm.name}: ${status::class.simpleName}")
                } else {
                    Text("Permission or status missing at index $index")
                }
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


