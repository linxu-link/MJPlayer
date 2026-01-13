import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "com.wj.convention.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    implementation(libs.truth)
    lintChecks(libs.androidx.lint.gradle)
}

// 启用严格的插件验证
tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

// 将自定义的 Kotlin Gradle 插件（Convention Plugins）注册为可被其他模块通过 plugins { id("...") } 方式引用的标准插件。
gradlePlugin {
    plugins {
        // 	注册一个插件，名称 "convention.dependency.graph" 是内部标识（可任意取）
        register("convention.dependency.graph") {
            id = libs.plugins.convention.dependency.graph.get().pluginId
            // 插件对应的 Kotlin 类名（必须是 Plugin<Project> 的实现类）
            implementationClass = "DependencyGraphPlugin"
        }

        register("convention.lint") {
            id = libs.plugins.convention.lint.get().pluginId
            implementationClass = "LintConventionPlugin"
        }

        register("convention.room") {
            id = libs.plugins.convention.room.get().pluginId
            implementationClass = "RoomAndroidConventionPlugin"
        }
        register("convention.hilt") {
            id = libs.plugins.convention.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("convention.android.test") {
            id = libs.plugins.convention.android.test.get().pluginId
            implementationClass = "AndroidTestConventionPlugin"
        }
        // app
        register("convention.app") {
            id = libs.plugins.convention.app.asProvider().get().pluginId
            implementationClass = "ApplicationAndroidConventionPlugin"
        }

        register("convention.app.compose") {
            id = libs.plugins.convention.app.compose.get().pluginId
            implementationClass = "ApplicationAndroidComposeConventionPlugin"
        }

        register("convention.app.jacoco") {
            id = libs.plugins.convention.app.jacoco.get().pluginId
            implementationClass = "ApplicationAndroidJacocoConventionPlugin"
        }
        register("convention.app.flavors") {
            id = libs.plugins.convention.app.flavors.get().pluginId
            implementationClass = "ApplicationAndroidFlavorsConventionPlugin"
        }
        // lib
        register("convention.lib") {
            id = libs.plugins.convention.lib.asProvider().get().pluginId
            implementationClass = "LibraryAndroidComposeConventionPlugin"
        }
        register("convention.lib.compose") {
            id = libs.plugins.convention.lib.compose.get().pluginId
            implementationClass = "LibraryAndroidComposeConventionPlugin"
        }
        register("convention.lib.jacoco") {
            id = libs.plugins.convention.lib.jacoco.get().pluginId
            implementationClass = "LibraryAndroidJacocoConventionPlugin"
        }
        register("convention.lib.jvm") {
            id = libs.plugins.convention.lib.jvm.get().pluginId
            implementationClass = "LibraryJvmConventionPlugin"
        }
        // feature
        register("convention.feature.impl") {
            id = libs.plugins.convention.feature.impl.get().pluginId
            implementationClass = "FeatureImplAndroidConventionPlugin"
        }
        register("convention.feature.api") {
            id = libs.plugins.convention.feature.api.get().pluginId
            implementationClass = "FeatureApiAndroidConventionPlugin"
        }
    }
}

