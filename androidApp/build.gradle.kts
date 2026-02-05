import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.chknkv.weightobserver"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.chknkv.weightobserver"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        resourceConfigurations += listOf("en", "ru")
    }

    signingConfigs {
        val releaseStoreFile = project.findProperty("RELEASE_STORE_FILE")?.toString()?.let { rootProject.file(it) }
        if (releaseStoreFile != null && releaseStoreFile.exists()) {
            create("release") {
                storeFile = releaseStoreFile
                storePassword = project.findProperty("RELEASE_STORE_PASSWORD")?.toString() ?: ""
                keyAlias = project.findProperty("RELEASE_KEY_ALIAS")?.toString() ?: ""
                keyPassword = project.findProperty("RELEASE_KEY_PASSWORD")?.toString() ?: ""
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            buildConfigField("boolean", "ENABLE_LOGS", "false")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.decompose)
    implementation(libs.decompose.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.napier)
}
