# 🔐 kPermissions

**kPermissions** is a Kotlin Multiplatform library that helps you manage runtime permissions on **Android** and **iOS**, with full modularity and optional support for **Jetpack Compose Multiplatform (CMP)**.

---

## 🚀 Features

- ✅ Android & iOS support
- 🧩 Clean `expect/actual` API structure
- 🧑‍💻 Easy to add custom permissions
- ⚙️ No Flow or Delegates required
- 🔁 No platform code inside `commonMain`
- 🧱 Compose UI support (optional)

---

## 📦 Module Structure

| Module | Purpose |
|--------|---------|
| `kpermissions-api`   | Shared interface definitions like `Permission`, `PermissionStatus`, etc. |
| `kpermissions-core`  | Compose UI support – automatically included in permission modules |
| `kpermissions-gallery`, `kpermissions-record-audio`, etc. | Ready-to-use permission modules for different needs |

---

## 🛠️ Installation

### For Kotlin Multiplatform

```kotlin
dependencies {
    implementation("io.github.kpermissions:kpermissions-gallery:<version>")
    // Add more as needed
}
```

## 📌 Note

⚠️ You do not need to add kpermissions-core manually.
It's included automatically inside each permission module via api(...).

but need add it as api and all permissions for export it if u need use native ui

## 🧩 Usage with Compose Multiplatform

val permission = rememberPermissionState(GalleryPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Permission granted
        } else {
            // ❌ Permission denied
        }
    }
}

## You can make custom permissions

### In common or shared we impl LocationPermission

```kotlin
import io.github.kPermissions_api.Permission

expect object RecordAudioPermission : Permission
```

### In androidMain impl

```kotlin
// androidMain/kotlin/RecordAudioPermission.android.kt

import android.Manifest
import io.github.kPermissions_api.Permission

actual object RecordAudioPermission : Permission {
    override val name: String = "record_audio"

    override val androidPermissionName: String = Manifest.permission.RECORD_AUDIO
}
```

### In iosMain impl

```kotlin
// iosMain/kotlin/RecordAudioPermission.ios.kt

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import platform.AVFoundation.*

private fun getRecordAudioPermissionStatus(): PermissionStatus {
    return when (AVAudioSession.sharedInstance().recordPermission) {
        AVAudioSessionRecordPermissionGranted -> PermissionStatus.Granted
        AVAudioSessionRecordPermissionDenied -> PermissionStatus.DeniedPermanently
        AVAudioSessionRecordPermissionUndetermined -> PermissionStatus.Denied
        else -> PermissionStatus.Denied
    }
}

internal fun requestRecordAudioPermission(): ((Boolean) -> Unit) -> Unit {
    return { callback ->
        AVAudioSession.sharedInstance().requestRecordPermission { granted ->
            callback(granted)
        }
    }
}

actual object RecordAudioPermission : Permission {
    override val name: String = "record_audio"

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = requestRecordAudioPermission()

    override fun getPermissionStatus(): PermissionStatus = getRecordAudioPermissionStatus()
}


```

📌 Notes
kpermissions-core is already included via api(...) in all official permissions — no need to add it manually.

### 🧱 Compose API Reference

```kotlin
@Composable
fun rememberPermissionState(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit,
): PermissionState

@Composable
fun rememberMultiplePermissionsState(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit,
): List<PermissionState>
Interface: PermissionState
```

```kotlin
interface PermissionState {
    val permission: Permission
    var status: PermissionStatus

    fun launchPermissionRequest()
    fun openAppSettings()
}
```

### 🧩 Optional: CMP Extension Module

To enable additional control like SDK version limits or platform ignoring for permissions in **Compose Multiplatform**, you can include the `kpermissions-cmp` module:

```kotlin
implementation("io.github.kpermissions:kpermissions-cmp:<version>")


This module allows you to:

- ✅ Set `minSdkVersion` / `maxSdkVersion` for each platform
- 🧠 Use `PlatformIgnore` to skip permissions on specific platforms (e.g. iOS-only or Android-only)
- 🔁 Easily manage cross-platform behavior from shared code

### 📄 License

MIT License

### 🙌 Contributions

We welcome contributions and suggestions!
Feel free to open issues or pull requests for bug fixes, enhancements, or new permission types.
