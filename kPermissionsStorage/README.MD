# 🗃️ kPermissionsStorage

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-storage)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-storage)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

**kPermissionsStorage** is a Kotlin Multiplatform module that provides permission handling for
accessing external storage, supporting both **read** and **write** access based on Android SDK
levels. iOS is treated as always granted.

This is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions) system.

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-storage:<version>")
}
```

Find the latest version:  
https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions_storage

---

## ✅ Supported Permissions

- `ReadStoragePermission`
- `WriteStoragePermission`

> iOS: Treated as granted.  
> Android: Uses proper `Manifest.permission.READ_EXTERNAL_STORAGE` and `WRITE_EXTERNAL_STORAGE`
> based on API levels.

---

## 🧪 Usage Example

```kotlin
val readState = rememberPermissionState(ReadStoragePermission)
val writeState = rememberPermissionState(WriteStoragePermission)

LaunchedEffect(Unit) {
    readState.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Read access granted
        }
    }

    writeState.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Write access granted
        }
    }
}
```

---

## 🔧 Implementation Notes

### On Android:

- **ReadStoragePermission**:
    - Uses `Manifest.permission.READ_EXTERNAL_STORAGE` below Android 13 (API < 33).
    - Not required on Android 13+ (scoped storage and media access is used).

- **WriteStoragePermission**:
    - Uses `Manifest.permission.WRITE_EXTERNAL_STORAGE` below Android 10 (API < 29).
    - Not required on API 29+ (due to scoped storage rules).

- `isServiceAvailable()` always returns `true`.

### On iOS:

- Permissions are simulated as **granted by default**.
- `isServiceAvailable()` always returns `true`.
- Internally marked with `PlatformIgnore.IOS` (for write permission).

---

## 🔒 Permissions Required

### Android

Add the following to your `AndroidManifest.xml` (if targeting older APIs):

```xml
<!-- Optional: Only required on older Android versions -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

> These permissions are only requested conditionally based on API level.

### iOS

No permission declarations required in `Info.plist`.  
Storage access is granted by default and handled by the sandbox model.

---