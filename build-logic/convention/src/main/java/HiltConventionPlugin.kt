import com.android.build.gradle.api.AndroidBasePlugin
import com.wj.convention.libs
import com.wj.convention.plugin_hilt
import com.wj.convention.plugin_jetbrains_kotlin_jvm
import com.wj.convention.plugin_ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_ksp)

            dependencies {
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }

            // Add support for Jvm Module, base on org.jetbrains.kotlin.jvm
            pluginManager.withPlugin(plugin_jetbrains_kotlin_jvm) {
                dependencies {
                    "implementation"(libs.findLibrary("hilt.core").get())
                }
            }

            /** Add support for Android modules, based on [AndroidBasePlugin] */
            pluginManager.withPlugin("com.android.base") {
                apply(plugin = plugin_hilt)
                dependencies {
                    "implementation"(libs.findLibrary("hilt.android").get())
                }
            }
        }
    }
}
