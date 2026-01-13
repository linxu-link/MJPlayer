package com.wj.convention

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project

/**
 * 如果没有androidTest文件夹，请为项目禁用不必要的Android仪器测试。
 * 否则，这些项目在编译、打包、安装和运行后，最终只会显示以下消息：正在AVD上启动0个测试。
 *
 * 注意：可以通过根据buildTypes和flavors检查其他潜在的sourceSets来改进这一点。
 */
internal fun LibraryAndroidComponentsExtension.disableUnnecessaryAndroidTests(
    project: Project,
) = beforeVariants {
    it.androidTest.enable = it.androidTest.enable
            && project.projectDir.resolve("src/androidTest").exists()
}
