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

include(":composeApp")
include(":kPermissionsCore")
include(":kPermissionsCamera")
include(":kPermissionsStorage")
include(":kPermissionsGallery")
include(":kPermissionsAudio")
include(":kPermissionsVideo")
include(":kPermissionsCMP")
include(":kPermissionsApi")
include(":kpermissionsLocationChecker")
include(":kpermissionsLocationWhenInUse")
include(":kpermissionsLocationAlways")
include(":kpermissionsCMPLocationAlways")
include(":kPermissionCMPLocationWhenInUse")
include(":kPermissionsNotification")
include(":kPermissionsBluetooth")
