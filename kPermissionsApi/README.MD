<h1 align="center">KPermissions</h1>
<br>

<div align="center">

<a href="https://opensource.org/licenses/Apache-2.0">
  <img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/>
</a>

<a href="https://android-arsenal.com/api?level=21" rel="nofollow">
  <img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" />
</a>

<img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
<img src="https://img.shields.io/badge/iOS-12%2B-blue.svg?logo=apple" alt="iOS 12+ Badge" />

<a href="https://github.com/the-best-is-best/">
  <img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/>
</a>

</div>

---

### KPermissionsApi is a Kotlin Multiplatform Mobile (KMM) package that provides a base permission contract for Android and iOS. It simplifies managing permissions in a shared codebase, allowing developers to request and handle permissions uniformly across platforms without duplicating code

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/KPermissionsApi)](https://central.sonatype.com/artifact/io.github.the-best-is-best/KPermissionsApi)

KPermissionsApi is available on `mavenCentral()`.

<br>

- **Note:** This package handles core permission contracts; additional permission handling packages
  may be needed for specific permissions.

- **Note:** Make sure to add the necessary permissions declarations in both Android
  `AndroidManifest.xml` and iOS `Info.plist`.

- **Important for Android users:**  
  To enable proper permission handling on Android, you **must** call:

```kotlin
  AndroidPermission.setActivity(this)
```

<br>

## Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissionsapi:1.0.0")
}
```

# 🔐 kPermissions

## You can make custom permissions

### In common or shared we impl RecordAudioPermission

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
