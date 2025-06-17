# 📷 kPermissionsCamera

kPermissionsCamera is a Kotlin Multiplatform permission module that provides support for Camera
access permission on Android and iOS.  
This module is part of the kPermissions ecosystem.

---

# kPermissionsCamera

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-camera)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-camera)  
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)  
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

---

## 📦 Installation

Add the following to your shared module dependencies:

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-camera:<version>")
}
```

Replace `<version>` with the latest version available on Maven Central.

---

## 🧩 Platform Support

| Platform | Status      |
|----------|-------------|
| Android  | ✅ Supported |
| iOS      | ✅ Supported |

---

## 🧪 Usage Example

```kotlin
val cameraPermission = rememberPermissionState(CameraPermission)

LaunchedEffect(Unit) {
    cameraPermission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Camera permission granted
        } else {
            // ❌ Permission denied
        }
    }
}
```

---

## 🔧 Implementation Notes

**On Android:**  
This uses `Manifest.permission.CAMERA`. It checks if the permission is granted and launches a
runtime request if needed.

**On iOS:**  
It checks the `AVCaptureDevice.authorizationStatus` for `.video` and requests permission if not
already granted.  
The result is mapped to the internal `PermissionStatus`.

---

## 🔒 Permissions Required

**Android:**  
Add the following to your `AndroidManifest.xml`:

```xml

<uses-permission android:name="android.permission.CAMERA" />
```

**iOS:**  
Add the following to your `Info.plist`:

```xml

<key>NSCameraUsageDescription</key><string>We need camera access to scan and take photos.</string>
```

---

