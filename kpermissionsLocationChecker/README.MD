# 📡 kPermissions Location Checker

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-location-checker)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-location-checker)
[![GitHub](https://img.shields.io/badge/GitHub-the--best--is--best/kPermissions-181717?logo=github)](https://github.com/the-best-is-best/kPermissions)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blueviolet?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)

`KPermissionsLocationChecker` is a Kotlin Multiplatform library that provides a reactive Flow to
monitor whether the location services are enabled on the device (Android and iOS).  
This module is part of the [kPermissions](https://github.com/the-best-is-best/kPermissions)
ecosystem.

---

## 📦 Installation

dependencies {
implementation("io.github.the-best-is-best:kpermissions-location-checker:<version>")
}

Replace <version> with the latest version available:  
https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions_location_checker

---

## ✅ Supported Platforms

| Platform | Status      |
|----------|-------------|
| Android  | ✅ Supported |
| iOS      | ✅ Supported |

---

## 🔄 Usage Example

import io.github.kpermissionslocationChecker.locationServiceEnabledFlow
import kotlinx.coroutines.flow.collectLatest

LaunchedEffect(Unit) {
locationServiceEnabledFlow.collectLatest { isEnabled ->
if (isEnabled) {
// ✅ Location services are ON
} else {
// ❌ Location services are OFF
}
}
}

---

## 🔧 Implementation Notes

- Android:  
  Uses LocationManager to observe the GPS/network provider status.

- iOS:  
  Uses CLLocationManager.locationServicesEnabled() and notifies when the app becomes active.

---
