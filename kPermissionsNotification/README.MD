# 🔔 kPermissionsNotification

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-notification)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-notification)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

**kPermissionsNotification** is a Kotlin Multiplatform module that provides notification permission
handling for Android (API 33+) and iOS.  
It is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions) ecosystem.

---

## 📦 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-notification:<version>")
}
```

Find the latest version:  
https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-notification

---

## ✅ Platform Support

| Platform | Status                           |
|----------|----------------------------------|
| Android  | ✅ Supported (API 33+)            |
| iOS      | ✅ Supported (with system prompt) |

---

## 🧪 Usage Example

```kotlin
val permission = rememberPermissionState(NotificationPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Notifications are allowed
        } else {
            // ❌ Permission denied
        }
    }
}
```

---

## 🔧 Implementation Notes

### On Android:

- Uses `android.Manifest.permission.POST_NOTIFICATIONS` from API 33 (Android 13+).
- Treated as granted on older versions.
- `isServiceAvailable()` always returns `true`.

### On iOS:

- Checks `UNUserNotificationCenter.currentNotificationCenter()` for current authorization status.
- Uses `requestAuthorizationWithOptions(...)` with options: `alert`, `badge`, `sound`.
- Maps status to internal `PermissionStatus` enum.
- Works for all iOS versions that support UNNotificationCenter (iOS 10+).

---

## 🔒 Permissions Required

### Android:

Add this to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

*Only required on Android 13+ (API 33+).*

### iOS:

Add this to your `Info.plist`:

```xml
<key>NSUserNotificationUsageDescription</key>
<string>We use notifications to keep you updated.</string>
```