import com.wj.convention.plugin_convention_lib
import com.wj.convention.plugin_jetbrains_kotlin_plugin_serialization
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class FeatureApiAndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_convention_lib)
            apply(plugin = plugin_jetbrains_kotlin_plugin_serialization)

            dependencies {
                "api"(project(":core:navigation"))
            }
        }
    }
}
