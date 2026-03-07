rootProject.name = "KPermissionsApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":sharedUI")
include(":androidSimple")
include(":kPermissionsCore")
include(":kPermissionsCamera")
include(":kPermissionsStorage")
include(":kPermissionsGallery")
include(":kPermissionsReadAudio")
include(":kPermissionsReadVideo")
include(":kPermissionsCMP")
include(":kPermissionsApi")
include(":kpermissionsLocationChecker")
include(":kpermissionsLocationWhenInUse")
include(":kpermissionsLocationAlways")
include(":kpermissionsCMPLocationAlways")
include(":kPermissionCMPLocationWhenInUse")
include(":kPermissionsNotification")
include(":kPermissionsBluetooth")
include(":kPermissionsCMPBluetooth")
include(":shared")
