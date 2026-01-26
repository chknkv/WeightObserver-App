plugins {
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.chknkv.coreauthentication"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreAuthentication"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.decompose)
            implementation(libs.napier)
            implementation(libs.multiplatform.settings)
            implementation(libs.koin.core)

            implementation(project(":core:core-designsystem"))
            implementation(project(":core:core-session"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.biometric)
            implementation(libs.koin.core)
        }

        iosMain.dependencies { }
    }
}
