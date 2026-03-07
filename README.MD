<h1 align="center">KPermissions</h1>
<br>

<div align="center">

<a href="https://opensource.org/licenses/Apache-2.0">
  <img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/>
</a>

<a href="https://android-arsenal.com/api?level=21" rel="nofollow">
  <img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" />
</a>

<img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
<img src="https://img.shields.io/badge/iOS-12%2B-blue.svg?logo=apple" alt="iOS 12+ Badge" />

<a href="https://github.com/the-best-is-best/">
  <img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/>
</a>

</div>

---

# 🔐 kPermissions

**kPermissions** is a Kotlin Multiplatform library that helps you manage runtime permissions on **Android** and **iOS**, with full modularity and optional support for **Jetpack Compose Multiplatform (CMP)**.

---

## 🚀 Features

- ✅ Android & iOS support
- 🧩 Clean `expect/actual` API structure
- 🧑‍💻 Easy to add custom permissions
- ⚙️ No Flow or Delegates required
- 🔁 No platform code inside `commonMain`
- 🧱 Compose UI support (optional)

---

## 📦 Module Structure

| Module                                                                        | Purpose                                                                  |
|-------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| `kPermissionsApi`                                                             | Shared interface definitions like `Permission`, `PermissionStatus`, etc. |
| `kPermissionsCore`                                                            | Base logic and iOS Manager                                               |
| `kPermissionsGallery`, `kPermissionsCamera`, `kPermissionsNotification`, etc. | Ready-to-use permission modules                                          |

### Two ways to integrate

| Approach                        | Shared module                              | iOS app                      | Android app     |
|---------------------------------|--------------------------------------------|------------------------------|-----------------|
| **KMP** (native SwiftUI)        | `sharedCode/build.gradle.kts` → SharedCode | `iosApp/iosApp/iOSApp.swift` | —               |
| **CMP** (Compose Multiplatform) | `sharedUI/build.gradle.kts` → ComposeApp   | `iosAppCMP`                  | `androidSimple` |

- **KMP**: Use `sharedCode` + `iosApp` for native SwiftUI iOS. Permissions via
  `IOSPermissionManager` + `PermissionsView`.
- **CMP**: Use `sharedUI` for iOS (`iosAppCMP`) and Android (`androidSimple`). Permissions via
  `rememberPermissionState` in Compose.

### Sample Apps & Shared Modules

| App / Module    | Framework              | UI Stack              | Purpose                                                                            |
|-----------------|------------------------|-----------------------|------------------------------------------------------------------------------------|
| `iosApp`        | SharedCode             | Native SwiftUI        | Sample iOS app using `IOSPermissionManager` + `PermissionsView`                    |
| `iosAppCMP`     | ComposeApp             | Compose Multiplatform | Sample iOS app using Compose UI (`ContentView` → `ComposeView`)                    |
| `androidSimple` | —                      | Compose Multiplatform | Sample Android app using sharedUI                                                  |
| `sharedCode`    | SharedCode.xcframework | —                     | KMP shared module; exports kPermissions APIs for **native iOS** (used by `iosApp`) |
| `sharedUI`      | ComposeApp.xcframework | Compose               | KMP Compose module with kPermissions CMP; used by `iosAppCMP` & `androidSimple`    |

---

## 🛠️ Installation

### For Kotlin Multiplatform

```kotlin
dependencies {
    implementation("io.github.kpermissions:kpermissions-gallery:<version>")
    // Add more as needed
}
```

## 📌 Notes

⚠️ You do not need to add `kPermissionsCore` manually. It's included automatically inside each
permission module via `api(...)`.

### KMP (native SwiftUI) — use `sharedCode` + `iosApp`

1. **`sharedCode/build.gradle.kts`** — export kPermissions so Swift can use them:

```kotlin
iosTarget.binaries.framework {
    baseName = "SharedCode"
    isStatic = true
    export(projects.kPermissionsApi)
    export(projects.kPermissionsCore)
    export(projects.kPermissionsCamera)
    export(projects.kPermissionsGallery)
    export(projects.kPermissionsNotification)
}
```

2. **`iosApp/iosApp/iOSApp.swift`** — entry point for native SwiftUI:

```swift
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            PermissionsView()
        }
    }
}
```

### CMP (Compose Multiplatform) — use `sharedUI`

1. **`sharedUI/build.gradle.kts`** — add kPermissions CMP modules in `commonMain`:

```kotlin
commonMain.dependencies {
    implementation(projects.kPermissionsCore)
    implementation(projects.kPermissionsCamera)
    implementation(projects.kPermissionsGallery)
    implementation(projects.kPermissionsNotification)
    implementation(projects.kPermissionsCMP)
}
```

2. **`iosAppCMP/iosApp/iOSApp.swift`** — CMP iOS entry point:

```swift
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

Android: `androidSimple` depends on `sharedUI`; no extra Swift setup.

---

## 🧩 Usage with Compose Multiplatform

In CMP apps (e.g. `iosAppCMP`, `androidSimple` via `sharedUI`), use `rememberPermissionState`:

```kotlin
val permission = rememberPermissionState(GalleryPermission)

LaunchedEffect(Unit) {
    permission.launchPermissionRequest { granted ->
        if (granted) {
            // ✅ Permission granted
        } else {
            // ❌ Permission denied
        }
    }
}
```

On iOS, the CMP UI is hosted via `ComposeView` (see `iosAppCMP/iosApp/ContentView.swift`).

---

## 🍎 iOS Sample Apps

| App                             | Entry Point                                          | UI                                      |
|---------------------------------|------------------------------------------------------|-----------------------------------------|
| **iosApp** (NativeUI)           | `iOSApp.swift` → `WindowGroup { PermissionsView() }` | Native SwiftUI + SharedCode             |
| **iosAppCMP** (KPermissionsApp) | `iOSApp.swift` → `WindowGroup { ContentView() }`     | Compose Multiplatform via `ComposeView` |

---

## 🍎 Usage with Native iOS (SwiftUI) - Arabic Example

If you are using the library in a native iOS project with SwiftUI (like `iosApp`), you can use
`IOSPermissionManager` to handle permissions:

```swift
import SwiftUI
import SharedCode

struct PermissionsView: View {
    @State private var cameraStatus: String = "غير معروف"
    @State private var notificationStatus: String = "لم يتم الطلب"
    @State private var galleryStatus: String = "غير معروف"

    var body: some View {
        ScrollView {
            VStack(spacing: 25) {
                Text("إدارة التصاريح")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.top)

                // قسم الإشعارات
                PermissionCard(
                    title: "الإشعارات",
                    status: notificationStatus,
                    icon: "bell.badge.fill",
                    color: .green,
                    action: { requestNotifications() }
                )

                // قسم الكاميرا
                PermissionCard(
                    title: "الكاميرا",
                    status: cameraStatus,
                    icon: "camera.fill",
                    color: .blue,
                    action: { requestCamera() }
                )

                // قسم المعرض
                PermissionCard(
                    title: "معرض الصور",
                    status: galleryStatus,
                    icon: "photo.on.rectangle.angled",
                    color: .purple,
                    action: { requestGallery() }
                )

                Spacer()
            }
            .padding()
        }
        .onAppear {
            requestNotifications()
        }
    }

    // --- الدوال الخاصة بطلب التصاريح ---

    private func requestCamera() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: CameraPermission())
                updateStatusText(status: status, for: "camera")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    private func requestNotifications() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: NotificationPermission())
                updateStatusText(status: status, for: "notification")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    private func requestGallery() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: GalleryPermission())
                updateStatusText(status: status, for: "gallery")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    // دالة لتحديث النصوص المعروضة في الواجهة بالعربي
    private func updateStatusText(status: PermissionStatus, for type: String) {
        let text: String
        switch status {
        case is PermissionStatusGranted:
            text = "مسموح"
        case is PermissionStatusDenied:
            text = "مرفوض"
        case is PermissionStatusDeniedPermanently:
            text = "مرفوض نهائياً"
        case is PermissionStatusUnavailable:
            text = "غير متوفر"
        default:
            text = "غير معروف"
        }

        // تحديث المتغير الصحيح بناءً على النوع
        DispatchQueue.main.async {
            if type == "camera" {
                self.cameraStatus = text
            } else if type == "notification" {
                self.notificationStatus = text
            } else if type == "gallery" {
                self.galleryStatus = text
            }
        }
    }

    private func handlePermissionResult(status: PermissionStatus) {
        if status is PermissionStatusDeniedPermanently {
            IOSPermissionManager.shared.openAppSettings()
        }
    }
}

// مكون فرعي للبطاقة
struct PermissionCard: View {
    let title: String
    let status: String
    let icon: String
    let color: Color
    let action: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Image(systemName: icon)
                    .foregroundColor(color)
                    .font(.headline)
                Text(title)
                    .font(.headline)
                Spacer()
                Text(status)
                    .font(.subheadline)
                    .foregroundColor(status == "مسموح" ? .green : (status == "مرفوض" ? .red : .gray))
            }

            Button(action: action) {
                Label("طلب إذن \(title)", systemImage: icon)
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 12)
                    .background(color)
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }
}
```

---

## 🛠️ Custom Permissions

### 1. Define in `commonMain`
```kotlin
import io.github.kPermissions_api.Permission

expect object RecordAudioPermission : Permission
```

### 2. Implement in `androidMain`
```kotlin
import android.Manifest
import io.github.kPermissions_api.Permission

actual object RecordAudioPermission : Permission {
    override val name: String = "record_audio"
    override val androidPermissionName: String = Manifest.permission.RECORD_AUDIO
}
```

### 3. Implement in `iosMain`
```kotlin
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import platform.AVFoundation.*

actual object RecordAudioPermission : Permission {
    override val name: String = "record_audio"

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            AVAudioSession.sharedInstance().requestRecordPermission { granted ->
                dispatch_async(dispatch_get_main_queue()) { callback(granted) }
            }
        }

    override fun getPermissionStatus(): PermissionStatus {
        return when (AVAudioSession.sharedInstance().recordPermission) {
            AVAudioSessionRecordPermissionGranted -> PermissionStatus.Granted
            AVAudioSessionRecordPermissionDenied -> PermissionStatus.DeniedPermanently
            AVAudioSessionRecordPermissionUndetermined -> PermissionStatus.Denied
            else -> PermissionStatus.Denied
        }
    }
}
```

---

### 📄 License

MIT License
