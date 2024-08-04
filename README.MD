<h1 align="center">KPermissions</h1><br>

<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=21" rel="nofollow"><img alt="API" src="https://camo.githubusercontent.com/0eda703da08220e08354f624a3fc0023f10416a302565c69c3759bf6e0800d40/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4150492d32312532422d627269676874677265656e2e7376673f7374796c653d666c6174" data-canonical-src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" style="max-width: 100%;"></a>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
		<img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Badge iOS" />
		<!-- <img src="https://img.shields.io/badge/Platform-JVM-8A2BE2.svg?logo=openjdk" alt="Badge JVM" />
    <img src="https://img.shields.io/badge/Platform-WASM%20%2F%20JS-yellow.svg?logo=javascript" alt="Badge JS" />
<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a> -->


</div>
<br> 
</br>

- KPermissions for request permission android or ios in compose multiplatform or KMM.



## Download

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/KPermissions)](https://central.sonatype.com/artifact/io.github.the-best-is-best/KPermissions)
KPermissions is available on `mavenCentral()`.


```kotlin
implementation("io.github.the-best-is-best:KPermissions:1.0.3")
```

## Permissions support

```kotlin

enum class EnumAppPermission {
    // Common for Android, iOS, JavaScript, and WebAssembly
    CALENDAR_READ,
    CALENDAR_WRITE,
    CONTACTS_WRITE,
    CONTACTS_READ,
    LOCATION,
    LOCATION_ALWAYS,
    LOCATION_WHEN_IN_USE,
    CAMERA,
    MICROPHONE,
    NOTIFICATION,
    BLUETOOTH,

    /**
     * Android only
     */
    PHONE,
    WRITE_STORAGE,
    READ_STORAGE,
    PHOTO,
    VIDEO,


    // iOS specific
    /**
     * iOS only: manage app tracking transparency
     */
    APP_TRACKING_TRANSPARENCY,
    GALLERY,
}
```

## how to use

- Need in android make init in android main

```kotlin
   PermissionHandler.init(this)
```

- Use in common in compose multiplatform or android main

```kotlin
    val permission = PermissionHandler()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            content = { Text("click test permissions") },

            onClick = {
                permission.requestPermission(EnumAppPermission.LOCATION) {
                    println("permission $it")
                    if (!it) {
                        permission.openAppSettings()
                    }
                }
            }
        )
    }

```