
description = "Core module with session api"

plugins {
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidLibrary {
        namespace = "com.chknkv.coresession"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    val xcfName = "core-sessionKit"
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings)
            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqldelight.primitive)

            implementation(project(":core:core-utils"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.security.crypto)
            implementation(libs.androidx.biometric)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings)
            implementation(libs.sqldelight.android)
        }

        iosMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings)
            implementation(libs.sqldelight.native)
        }
    }
}

sqldelight {
    databases {
        create("WeightDatabase") {
            packageName.set("com.chknkv.coresession.db")
        }
    }
}
