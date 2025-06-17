# 📍 kPermissionsAlways

**kPermissionsAlways** is a Kotlin Multiplatform (KMP) permission module that provides access to *
*location always** permissions on Android and iOS.

This module is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions)
ecosystem.

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-always)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-always)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

---

## 📦 Installation

Add the dependency to your shared module:

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-always:<version>")
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
val permission = rememberPermissionState(LocationAlwaysPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Location always access granted
        } else {
            // ❌ Permission denied
        }
    }
}
```

---

## 🔧 Implementation Notes

### Android:

- Uses both `ACCESS_FINE_LOCATION` and `ACCESS_BACKGROUND_LOCATION`.
- On Android 10+ (API 29+), requesting background location requires a second step after requesting
  foreground.

### iOS:

- Uses `CLLocationManager` to request `Always` access.
- Handles transitions from `When In Use` to `Always` explicitly.
- Supports handling iOS-specific flows and required app settings checks.

---

## 🔒 Permissions Required

### Android

In `AndroidManifest.xml`:

```xml

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /><uses-permission
android:name="android.permission.ACCESS_COARSE_LOCATION" /><uses-permission
android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

### iOS

In `Info.plist`:

```xml

<key>NSLocationAlwaysAndWhenInUseUsageDescription</key><string>We need your location at all times
for background tracking.
</string><key>NSLocationWhenInUseUsageDescription</key><string>We need your location to provide
relevant features while the app is in use.
</string>
```
