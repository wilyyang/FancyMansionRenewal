plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.compose)
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.fancymansion.core.presentation"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.bom.compose))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.material)
    implementation(libs.bundles.coil)

    implementation(project(":core:common"))
}