# kPermissionsCore

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-core)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-core)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Platform Support](https://img.shields.io/badge/platform-Android%20%7C%20iOS-green)

**Core permission management for Kotlin Multiplatform (KMP) with Compose support.**  
This package provides the essential composable APIs and utilities for requesting single and multiple
permissions, checking statuses, handling unavailable services, and navigating to system settings on
both Android and iOS.

---

🚀 Installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-core:<latest-version>")
}

```

## ✅ Features

- 🔁 `rememberPermissionState` – Request and observe single permission status.
- 🔁 `rememberMultiplePermissionsState` – Request and observe multiple permissions together.
- 🎯 `requestPermissionsSequentially` – Sequential `suspend` handling of permission chains.
- 📲 `openAppSettingsPlatform` – Open app system settings (Android & iOS).
- 🔁 `OnAppResumed` – Resume callback when returning from app settings.
- 🧠 `getStatus` – Smart status evaluation with service availability.

## 🧩 Usage

- Request single permission:

```kotlin
val permissionState = rememberPermissionState(CameraPermission) { granted ->
    if (granted) {
        // Permission granted
    } else {
        // Permission denied
    }
}

```

- Request multiple permissions:

```kotlin
val permissionsState = rememberMultiplePermissionsState(
    listOf(CameraPermission, LocationPermission)
) { allGranted ->
    if (allGranted) {
        // All permissions granted
    } else {
        // Some permissions denied
    }
}

```

### Open app settings

```kotlin
openAppSettingsPlatform()
```

### 🧠 Smart Status Handling

#### This includes logic for

- Ignored permissions

- Unavailable services

- Permanent denial detection
