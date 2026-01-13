pluginManagement {
    includeBuild("build-logic")
    repositories {
        // 添加阿里云maven中央仓库镜像
        maven(url = "https://maven.aliyun.com/repository/central")
        // 添加阿里云google仓库镜像
        maven(url = "https://maven.aliyun.com/repository/google")
        // 添加阿里云gradle插件仓库镜像
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 添加阿里云maven中央仓库镜像
        maven(url = "https://maven.aliyun.com/repository/central")
        // 添加阿里云google仓库镜像
        maven(url = "https://maven.aliyun.com/repository/google")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MagicPlayer"
//include(":app")
include(":mediaService")
include(":toolkit")
include(":player")
include(":lint")
