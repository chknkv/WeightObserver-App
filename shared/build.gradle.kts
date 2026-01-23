description = "Shared module of WeightObserver-MobileApp application"

plugins {
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "com.chknkv.weightobserver.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "WeightObserverApp"
            isStatic = false
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.decompose)
            implementation(libs.decompose.compose.extensions)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.napier)

            api(project(":core:core-designsystem"))
            api(project(":core:core-utils"))
            api(project(":core:core-session"))

            api(project(":feature:feature-welcome"))
            api(project(":feature:feature-main"))
        }

        iosMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.napier)
        }
    }
}
