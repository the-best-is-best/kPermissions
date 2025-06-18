# 🖼️ kPermissionsGallery

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kpermissions-gallery)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-gallery)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
![Supported Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-green)

kPermissionsGallery is a Kotlin Multiplatform permission module that provides access to the
gallery (external image picker) on Android and iOS.  
This module is part of the kPermissions ecosystem.

---

## 📦 Installation

dependencies {
implementation("io.github.the-best-is-best:kpermissions-gallery:<version>")
}

---

## ✅ Platform Support

| Platform | Status      |
|----------|-------------|
| Android  | ✅ Supported |
| iOS      | ✅ Supported |

---

## 🧪 Usage Example

val permission = rememberPermissionState(GalleryPermission)

LaunchedEffect(Unit) {
permission.launchPermissionRequest()
}

---

## 🔧 Implementation Notes

- Android:
  Requests android.permission.READ_EXTERNAL_STORAGE (on API <33) or READ_MEDIA_IMAGES (on API 33+).
  Note: Some devices may require WRITE_EXTERNAL_STORAGE for full functionality before Android 10.

- iOS:
  Uses PHPickerViewController on iOS 14+ or UIImagePickerController on older versions.
  No explicit permission needed, but user must allow Photos access via system prompt.

---

## 🔒 Permissions Required

### Android

Add to your AndroidManifest.xml:

<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

(Optional for older SDKs):

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

### iOS

Add to Info.plist:

<key>NSPhotoLibraryUsageDescription</key>
<string>We need access to your photo gallery to select images.</string>

---

## 🔗 See Also

GitHub: <https://github.com/the-best-is-best/kPermissions>  
Maven
Central: <https://central.sonatype.com/artifact/io.github.the-best-is-best/kpermissions-gallery>

---

## 🛡 License

MIT License
