import com.wj.convention.configureGraphTasks
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 为所有子模块批量注册依赖图生成与文档更新任务，实现模块依赖关系的可视化与自动化维护。\
 * 该插件会在根项目应用时，为所有子模块（包括测试模块）注册以下任务：
 * - 生成依赖关系文件（txt 格式）
 * - 将生成的依赖关系文件转换为 Markdown 格式文档
 */
class DependencyGraphPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // 强制要求插件应用在根项目，避免在子模块中误用
        require(target.path == ":")
        // 为所有子模块配置依赖图任务，包括测试模块
        target.subprojects { configureGraphTasks() }
    }

}
