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
