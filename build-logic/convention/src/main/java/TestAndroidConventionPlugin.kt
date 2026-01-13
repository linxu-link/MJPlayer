import com.android.build.api.dsl.TestExtension
import com.wj.convention.TARGET_SDK
import com.wj.convention.configureGradleManagedDevices
import com.wj.convention.configureKotlinAndroid
import com.wj.convention.plugin_android_test
import com.wj.convention.plugin_jetbrains_kotlin_android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class TestAndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_android_test)
            apply(plugin = plugin_jetbrains_kotlin_android)

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = TARGET_SDK
                configureGradleManagedDevices(this)
            }
        }
    }
}
