// 1. 定义工具版本
val ktlintVersion = "0.50.0"  // ktlint 版本（Kotlin 代码风格检查工具）

// 2. 初始化脚本的依赖配置
initscript {
    val spotlessVersion = "6.25.0"  // Spotless 插件版本（代码格式化插件）
    repositories {
        mavenCentral()  // 从 Maven 仓库下载插件
    }
    dependencies {
        // 引入 Spotless 插件，用于后续在项目中应用
        classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
    }
}

// 3. 对项目所有子模块应用配置
rootProject {
    subprojects {  // 遍历所有子项目
        apply<com.diffplug.gradle.spotless.SpotlessPlugin>()  // 应用 Spotless 插件

        // 配置 Spotless 插件规则
        extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            // 配置 Kotlin 代码格式化
            kotlin {
                target("**/*.kt")
                targetExclude("**/build/**/*.kt")
                targetExclude("**/test/**/*.kt")
                targetExclude("**/androidTest/**/*.kt")

                ktlint(ktlintVersion)
                    .editorConfigOverride(
                        mapOf(
                            "android" to "true",
                            "max_line_length" to "120",
                            "indent_size" to "4"
                        )
                    )

                licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
            }


            // 配置 .kts 脚本文件（如 build.gradle.kts）的格式化
            format("kts") {
                target("**/*.kts")
                targetExclude("**/build/**/*.kts")
                licenseHeaderFile(
                    rootProject.file("spotless/copyright.kts"),
                    "(^(?![\\/ ]\\*).*$)"
                )  // 同样添加版权头
            }

            // 配置 XML 文件（如布局文件、清单文件）的格式化
            format("xml") {
                target("**/*.xml")
                targetExclude("**/build/**/*.xml")
                licenseHeaderFile(
                    rootProject.file("spotless/copyright.xml"),
                    "(<[^!?])"
                )  // 为 XML 添加版权头（如注释形式的版权声明）
            }
        }
    }
}
