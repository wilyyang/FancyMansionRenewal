plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.compose)
    alias(libs.plugins.com.google.dagger.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.fancymansion.test"

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

    defaultConfig {
        kapt {
            arguments {
                arg("room.incremental", "true")
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    kapt(libs.hilt.android.compiler)

    implementation(platform(libs.bom.compose))
    implementation(libs.bundles.compose)

    implementation(libs.datastore)
    implementation(libs.bundles.network)
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.test)

    implementation(project(":core:common"))
    implementation(project(":data"))
    implementation(project(":di:injectRepository"))
    implementation(project(":domain:model"))
    implementation(project(":domain:interfaceRepository"))
}