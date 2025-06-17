# 📡 kPermissions CMP Bluetooth

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-cmp-bluetooth)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-cmp-bluetooth)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

**kPermissions-CMP-Bluetooth** is an optional helper module for `kPermissionsBluetooth`.  
It provides cross-platform support for opening the Bluetooth settings screen from shared Kotlin
Multiplatform code.

### Gradle installation

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-cmp-bluetooth:<version>")
}
```

---

## 📄 Description

> Plugin for requesting Bluetooth permission and navigating to Bluetooth settings on Android and iOS
> using Kotlin Multiplatform Mobile (KMM).

---

## 🚀 Usage Example

```kotlin
import io.github.kpermissionsbluetooth.BluetoothPermission
import io.github.kpermissionscmpbluetooth.openBluetoothSettingsCMP

BluetoothPermission.openBluetoothSettingsCMP()
```

---

## 🔧 Implementation Notes

### On Android

- Uses `Intent(Settings.ACTION_BLUETOOTH_SETTINGS)` to open Bluetooth settings.

### On iOS

- Opens the `App-Prefs:root=Bluetooth` URL using `UIApplication.sharedApplication.openURL()` if
  available.

---

## 📚 License

This library is distributed under the MIT License. See [LICENSE](LICENSE) for more information.
