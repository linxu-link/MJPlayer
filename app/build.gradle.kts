/*
 * Copyright 2025 WuJia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.wj.player"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wj.player"
        minSdk = 26
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    lint {
        disable.add("NullSafeMutableLiveData")
        // 也可以同时禁用其他可能有问题的检查
        disable.add("GradleDependency")
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":toolkit"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.lifecycle)
    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    // paging + room
    implementation(libs.bundles.paging)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    // workManager
    implementation(libs.androidx.workmanager)
    // startUp
    implementation(libs.androidx.startup.runtime)
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)
    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // webview
    implementation(libs.accompanist.webview)
    // media
    implementation(libs.bundles.media)
    // dataStore
    implementation(libs.bundles.datastore)
    // hilt
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.timber)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}