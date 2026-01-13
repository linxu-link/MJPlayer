package com.wj.convention

/*
* Plugin 中配置的各类常量。
*/

const val TARGET_SDK = 36
const val MIN_SDK = 24
const val COMPILE_SDK = 36

// 原生插件
const val plugin_android_application = "com.android.application"
const val plugin_android_library = "com.android.library"
const val plugin_jetbrains_kotlin_android = "org.jetbrains.kotlin.android"
const val plugin_jetbrains_kotlin_plugin_compose = "org.jetbrains.kotlin.plugin.compose"
const val plugin_android_lint = "com.android.lint"
const val plugin_dropbox_dependency_guard = "com.dropbox.dependency-guard"
const val plugin_jacoco = "jacoco"
const val plugin_jetbrains_kotlin_jvm = "org.jetbrains.kotlin.jvm"
const val plugin_ksp = "com.google.devtools.ksp"
const val plugin_android_test = "com.android.test"
const val plugin_jetbrains_kotlin_plugin_serialization = "org.jetbrains.kotlin.plugin.serialization"
const val plugin_hilt = "dagger.hilt.android.plugin"
const val plugin_android_room = "androidx.room"

// 自定义插件
const val plugin_convention_dependency_graph = "convention.dependency.graph"
const val plugin_convention_lint = "convention.lint"
const val plugin_convention_room = "convention.room"
const val plugin_convention_hilt = "convention.hilt"
const val plugin_convention_android_test = "convention.android.test"
const val plugin_convention_app = "convention.app"
const val plugin_convention_app_compose = "convention.app.compose"
const val plugin_convention_app_jacoco = "convention.app.jacoco"
const val plugin_convention_app_flavors = "convention.app.flavors"
const val plugin_convention_lib = "convention.lib"
const val plugin_convention_lib_compose = "convention.lib.compose"
const val plugin_convention_lib_jacoco = "convention.lib.jacoco"
const val plugin_convention_lib_jvm = "convention.lib.jvm"

const val plugin_convention_feature = "convention.feature"
const val plugin_convention_feature_impl = "convention.feature.impl"
const val plugin_convention_feature_api = "convention.feature.api"
