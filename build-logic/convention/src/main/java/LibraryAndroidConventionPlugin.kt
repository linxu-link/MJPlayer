import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.wj.convention.configureFlavors
import com.wj.convention.configureGradleManagedDevices
import com.wj.convention.configureKotlinAndroid
import com.wj.convention.configurePrintApksTask
import com.wj.convention.disableUnnecessaryAndroidTests
import com.wj.convention.libs
import com.wj.convention.plugin_android_library
import com.wj.convention.plugin_convention_lint
import com.wj.convention.plugin_jetbrains_kotlin_android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


class LibraryAndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // 应用 Android 库插件
            apply(plugin = plugin_android_library)
            // 应用 Kotlin Android 插件
            apply(plugin = plugin_jetbrains_kotlin_android)

            /**
             * 应用自定义 Lint Android 插件
             * @see LintConventionPlugin
             */
            apply(plugin = plugin_convention_lint)

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 36
                lint.targetSdk = 36
                defaultConfig.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                configureFlavors(this)
                configureGradleManagedDevices(this)
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())
                "testImplementation"(libs.findLibrary("kotlin.test").get())
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
            }

        }
    }

}
