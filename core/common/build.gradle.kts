
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)

    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 23
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

android {
    namespace = "com.fancymansion.core.common"
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}