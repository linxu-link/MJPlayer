// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
    // Dropbox 开源的一个 Gradle 插件，用于 监控和控制项目中依赖项（dependencies）的大小变化，
    // 可以帮助开发人员识别和解决依赖项大小增加的问题，从而优化应用程序的性能和大小。
    alias(libs.plugins.dependencyGuard) apply false
    // 自定义插件 生成 依赖关系图（README.md）
    // 执行 ./gradlew graphDump
    // 执行 ./gradlew graphUpdate
    alias(libs.plugins.convention.dependency.graph)
}
