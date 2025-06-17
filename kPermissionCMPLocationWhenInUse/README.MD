# 🧭 kPermissionsCmpLocationWhenInUse

**kPermissionsCmpLocationWhenInUse** is a Compose Multiplatform (CMP) module for managing the
`LocationInUsePermission`.  
It builds on top of the core `kPermissions` system to offer Compose integration for requesting and
handling **location while in use** permission on Android and iOS.

Part of the [kPermissions](https://github.com/the-best-is-best/kPermissions) ecosystem.

---

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-cmp-location-when-in-use)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-cmp-location-when-in-use)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-cmp-location-when-in-use:<version>")
}
```

Replace `<version>` with the latest version from Maven Central.

---

## 🧩 Platform Support

| Platform | Support                                |
|----------|----------------------------------------|
| Android  | ✅ Supported via `ACCESS_FINE_LOCATION` |
| iOS      | ✅ Supported using `CLLocationManager`  |

---

## 🧪 Usage Example

```kotlin
val permission = rememberPermissionState(LocationInUsePermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Access granted to location
        } else {
            // ❌ Permission denied
        }
    }
}
```

### Used for open privacy location settings

```kotlin
scope.launch {
    LocationInUsePermission.openPrivacySettings()
}
```

You can also observe permission status and update your UI accordingly.

---

## 🔒 Permissions Required

### Android:

Add this to your `AndroidManifest.xml`:

```xml

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### iOS:

Add this to your `Info.plist`:

```xml

<key>NSLocationWhenInUseUsageDescription</key><string>We need your location to provide services
while the app is in use.
</string>
```
