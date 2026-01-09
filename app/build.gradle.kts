import java.util.Date
import java.text.SimpleDateFormat

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.compose)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    namespace = "com.fancymansion.app"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    buildFeatures {
        buildConfig = true
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

    val formattedDate: String = SimpleDateFormat("yyMMdd").format(Date())
    val code = formattedDate.toInt() * 100

    defaultConfig {
        applicationId = "com.fancymansion.app"
        versionCode = 114
        versionName = "1.0.0"
        setProperty("archivesBaseName", "FancyMansion_${versionName}_$code")
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(platform(libs.bom.firebase))
    implementation(libs.bundles.firebase)

    implementation(platform(libs.bom.compose))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.material)
    implementation(libs.bundles.navigation)

    implementation(project(":core:common"))
    implementation(project(":core:presentation"))
    implementation(project(":domain:usecase"))
    implementation(project(":presentation:main"))
    implementation(project(":presentation:viewer"))
    implementation(project(":presentation:bookOverview"))
    implementation(project(":presentation:editor"))
}
