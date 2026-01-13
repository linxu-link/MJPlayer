import com.wj.convention.configureKotlinJvm
import com.wj.convention.libs
import com.wj.convention.plugin_convention_lint
import com.wj.convention.plugin_jetbrains_kotlin_jvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class LibraryJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_jetbrains_kotlin_jvm)
            apply(plugin = plugin_convention_lint)

            configureKotlinJvm()
            dependencies {
                "testImplementation"(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
