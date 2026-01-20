import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
}

android {
    namespace = "org.company.app.androidApp"
    compileSdk = 36

    packaging {
        resources {
            pickFirsts += "**/*.cvr"
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile =
                file("/Users/michelleraouf/Desktop/kmm/kotlin-openid/simple/androidSimple/key")
            storePassword = "key-pass"
            keyPassword = "key-pass"
            keyAlias = "key0"
        }
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 36

        applicationId = "org.company.app.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        addManifestPlaceholders(
            mapOf("appAuthRedirectScheme" to "com.duendesoftware.demo")
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}



kotlin {

    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(projects.sharedUI)
    implementation(libs.androidx.activity.compose)

}

