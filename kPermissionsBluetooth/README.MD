# kPermissionsBluetooth

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-bluetooth)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-bluetooth)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

kPermissionsBluetooth provides Bluetooth permission support for the kPermissions library using
Kotlin Multiplatform.

✅ Features:

- Support for requesting Bluetooth permission.
- Flow<Boolean> to monitor Bluetooth state.
- Works on Android and iOS.

📦 Installation:

implementation("io.github.the-best-is-best:kpermissions-bluetooth:1.0.0")

🧩 Usage:

To request the Bluetooth permission:

```kotlin
val state = rememberPermissionState(BluetoothPermission)
```

To observe Bluetooth state:

val isBluetoothOnFlow: Flow<Boolean> = bluetoothStateFlow()

🛠 Requirements:

- Kotlin Multiplatform
- Android: API 21+
- iOS: iOS 13+

📄 License:

MIT License. See LICENSE for details.
