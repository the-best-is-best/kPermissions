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
            baseName = "SharedCode"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {

        }
        commonMain.dependencies {

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
    android {
        namespace = "io.github.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}


