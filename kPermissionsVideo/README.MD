# 🎞️ kPermissionsVideo

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-video)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-video)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

**kPermissionsVideo** is a Kotlin Multiplatform module that handles permission for reading videos
from media storage. It's part of
the [kPermissions](https://github.com/the-best-is-best/kPermissions) ecosystem.

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-video:<version>")
}
```

Find the latest version here:  
https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions_video

---

## ✅ Permission

- `ReadVideoPermission`

> iOS: Marked as granted by default.  
> Android: Requests `READ_MEDIA_VIDEO` (API 33+ only).

---

## 🧪 Usage

```kotlin
val videoState = rememberPermissionState(ReadVideoPermission)

LaunchedEffect(Unit) {
    videoState.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Access to videos granted
        }
    }
}
```

---

## 🔧 Implementation Notes

### On Android:

- Targets Android 13+ (API 33+) with:
  ```kotlin
  android.Manifest.permission.READ_MEDIA_VIDEO
  ```
- Older versions do **not** require explicit video access permission.
- `isServiceAvailable()` always returns `true`.

### On iOS:

- No actual permission required.
- Marked as `Granted` by default using `PlatformIgnore.IOS`.

---

## 🔒 Permissions Required

### Android

Add this to `AndroidManifest.xml` for Android 13+:

```xml
<uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
```

> Not required for Android < 13.

### iOS

No permissions required or requested. Always granted.
