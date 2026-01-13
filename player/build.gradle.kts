import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.convention.app)
    alias(libs.plugins.convention.app.compose)
    alias(libs.plugins.convention.room)
    alias(libs.plugins.convention.hilt)
}

android {
    defaultConfig {
        applicationId = "com.wj.player"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlin {
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_17)
//        }
//    }
//    buildFeatures {
//        compose = true
//    }
}

dependencies {
    // androidx
    implementation(libs.bundles.androidx.base)
    // accompanist
    implementation(libs.bundles.accompanist)
    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.compose.navigation)
    // paging + room
    implementation(libs.bundles.paging)
    implementation(libs.bundles.room)
    // workManager
    implementation(libs.androidx.workmanager)
    // startUp
    implementation(libs.androidx.startup.runtime)
    // media
    implementation(libs.bundles.media)
    // dataStore
    implementation(libs.bundles.datastore)
    // hilt
    implementation(libs.bundles.hilt)
    // timber
    implementation(libs.timber)
    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // retrofit+okhttp
    implementation(libs.bundles.net)
    implementation(libs.compose.cloudy)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}
