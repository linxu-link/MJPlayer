import com.android.build.api.dsl.LibraryExtension
import com.wj.convention.configureAndroidCompose
import com.wj.convention.plugin_android_library
import com.wj.convention.plugin_jetbrains_kotlin_plugin_compose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class LibraryAndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = plugin_android_library)
            apply(plugin = plugin_jetbrains_kotlin_plugin_compose)

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }

}
