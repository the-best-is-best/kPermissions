# 🛰️ kPermissionsCmpLocationAlways

**kPermissionsCmpLocationAlways** is a Compose Multiplatform (CMP) module for managing the
`LocationAlwaysPermission`.  
It integrates with the `kPermissions` system to request and handle **location always** permission on
both Android and iOS.

This module belongs to the [kPermissions](https://github.com/the-best-is-best/kPermissions)
ecosystem.

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-cmp-location-always)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-cmp-location-always)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-cmp-location-always:<version>")
}
```

Replace `<version>` with the latest version from Maven Central.

---

## 🧩 Platform Support

| Platform | Support                                  |
|----------|------------------------------------------|
| Android  | ✅ `ACCESS_BACKGROUND_LOCATION` (API 29+) |
| iOS      | ✅ Full support via `CLLocationManager`   |

---

## 🧪 Usage Example

```kotlin
val permission = rememberPermissionState(LocationAlwaysPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Background location access granted
        } else {
            // ❌ Permission denied
        }
    }
}

```

### Used for open privacy location settings

```kotlin
scope.launch {
    LocationAlwaysPermission.openPrivacySettings()
}
```

---

## 🔒 Permissions Required

### Android:

In your `AndroidManifest.xml`:

```xml

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /><uses-permission
android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

### iOS:

In your `Info.plist`:

```xml

<key>NSLocationAlwaysAndWhenInUseUsageDescription</key><string>We require your location even when
the app is in the background.
</string><key>NSLocationAlwaysUsageDescription</key><string>This app needs location access at all
times.
</string>
```
