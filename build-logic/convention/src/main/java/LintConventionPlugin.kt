import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import com.wj.convention.plugin_android_application
import com.wj.convention.plugin_android_library
import com.wj.convention.plugin_android_lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class LintConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // 根据项目类型，选择不同的方式配置 Lint
            when {
                pluginManager.hasPlugin(plugin_android_application) ->
                    configure<ApplicationExtension> { lint(Lint::configure) }

                pluginManager.hasPlugin(plugin_android_library) ->
                    configure<LibraryExtension> { lint(Lint::configure) }

                else -> {
                    // 非 Android 项目，直接在顶层配置 Lint 插件（因为不需要 android{} 块）
                    apply(plugin = plugin_android_lint)
                    configure<Lint>(Lint::configure)
                }
            }
        }
    }
}

// 统一的 Lint 配置内容
private fun Lint.configure() {
    xmlReport = true
    sarifReport = true
    checkDependencies = true
    disable += "GradleDependency"
}

