# ⚙️ kPermissions CMP

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-cmp)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-cmp)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

**kPermissions CMP** is the core platform helper module for integrating permission management in
Kotlin Multiplatform (KMM) applications.  
It provides shared logic and utilities for platform-specific permission status control, including
handling ignored platforms and common status refresh helpers.

---

### Add to your Gradle dependencies

```kotlin
dependencies {
    implementation("io.github.the-best-is-best:kpermissions-cmp:<version>")
}
```

Replace `<version>` with the latest published version.

---

## 📄 Description

> Plugin for requesting permissions (e.g., Camera, Audio, etc.) in Android and iOS using Kotlin
> Multiplatform Mobile (KMM).  
> This module contains core Compose-based permission management and cross-platform helpers.

---

## 🧩 Key Features

- `PlatformIgnore`: Ignore a permission implementation on either Android or iOS when not applicable.
- `refreshStatusCMP()`: Common suspend function to refresh the status of any permission from shared
  code.
- Shared permission map and ignore logic.
- Useful when building multiplatform UI logic around permissions.

---

## 🧪 Usage Example

### Ignoring a Permission on iOS

```kotlin
actual object ReadAudioPermission : Permission {
    init {
        setIgnore(PlatformIgnore.IOS)
    }

    ...
}
```

### Checking Ignore Status

```kotlin
val shouldIgnore = permission.getIgnore() == PlatformIgnore.IOS
```

---

It is **not meant to be used directly** by app-level code unless you are building your own
permission handlers.
