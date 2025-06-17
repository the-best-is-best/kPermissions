# 🎵 kPermissionsAudio

**kPermissionsAudio** is a Kotlin Multiplatform permission module that provides support for the
`ReadAudio` permission on **Android** (API 33+).  
This module is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions)
ecosystem.

---

# kPermissionsAudio

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-audio)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-audio)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

- `kPermissionsAudio` provides access to the READ_MEDIA_AUDIO permission on Android 13+ only. It is
  ignored on iOS.

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-audio:<version>")
}
```

## 🧩 Platform Support

| Platform | Status                                |
|----------|---------------------------------------|
| Android  | ✅ Supported (API 33+)                 |
| iOS      | 🚫 Ignored (via `PlatformIgnore.IOS`) |

## 🧪 Usage Example

```kotlin
val permission = rememberPermissionState(ReadAudioPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Access granted to audio media
        } else {
            // ❌ Permission denied
        }
    }
}

```

## 🔧 Implementation Notes

On Android:
Uses Manifest.permission.READ_MEDIA_AUDIO starting from API 33 (Android 13+). For older versions,
the permission is treated as granted by default.

On iOS:
Permission is marked as ignored and always returns Granted internally – there’s no equivalent
permission.
