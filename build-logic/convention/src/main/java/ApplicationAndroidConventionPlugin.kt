import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.wj.convention.TARGET_SDK
import com.wj.convention.configureBadgingTasks
import com.wj.convention.configureGradleManagedDevices
import com.wj.convention.configureKotlinAndroid
import com.wj.convention.configurePrintApksTask
import com.wj.convention.plugin_android_application
import com.wj.convention.plugin_convention_lint
import com.wj.convention.plugin_dropbox_dependency_guard
import com.wj.convention.plugin_jetbrains_kotlin_android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ApplicationAndroidConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_android_application)
            apply(plugin = plugin_jetbrains_kotlin_android)
            apply(plugin = plugin_convention_lint)
            apply(plugin = plugin_dropbox_dependency_guard)

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configureBadgingTasks(this)
            }
        }
    }

}
