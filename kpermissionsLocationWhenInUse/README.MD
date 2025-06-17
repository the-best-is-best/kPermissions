# 📍 kPermissionsWhenInUse

[kPermissionsWhenInUse] is a Kotlin Multiplatform (KMP) permission module that provides access to *
*location when in use** permissions on Android and iOS.

This module is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions)
ecosystem.

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-when-in-use)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-when-in-use)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

---

## 📦 Installation

Add the dependency to your shared module:

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-when-in-use:<version>")
}
```

Replace `<version>` with the latest version shown on Maven Central.

---

## 🧩 Platform Support

| Platform | Status      |
|----------|-------------|
| Android  | ✅ Supported |
| iOS      | ✅ Supported |

---

## 🧪 Usage Example

```kotlin
val permission = rememberPermissionState(LocationInUsePermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Location access granted
        } else {
            // ❌ Permission denied
        }
    }
}
```

---

## 🔧 Implementation Notes

### Android

- Uses `Manifest.permission.ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`.
- Checks for permission status and launches the system dialog as needed.
- On Android 10+ (API 29+), fine location must be declared explicitly.

### iOS

- Uses `CLLocationManager` to request location when in use.
- Handles all native iOS permission status cases including:
    - `Not Determined`
    - `Restricted`
    - `Denied`
    - `Authorized When In Use`

---

## 🔒 Permissions Required

### Android

In `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### iOS

In `Info.plist`:

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>We need your location to provide relevant features while the app is in use.</string>
```
