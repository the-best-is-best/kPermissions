plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)


    listOf(
//        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(projects.kPermissionsCore)
            implementation(projects.kPermissionsCamera)
            implementation(projects.kPermissionsStorage)
            implementation(projects.kPermissionsGallery)
            implementation(projects.kPermissionsReadAudio)
            implementation(projects.kPermissionsReadVideo)
            implementation(projects.kPermissionCMPLocationWhenInUse)
            implementation(projects.kpermissionsCMPLocationAlways)
            implementation(projects.kpermissionsLocationChecker)
            implementation(projects.kPermissionsNotification)
            implementation(projects.kPermissionsCMPBluetooth)

            implementation(projects.kPermissionsCMP)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
    android {
        namespace = "io.github.sample"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}


