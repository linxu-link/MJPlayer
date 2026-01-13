package com.wj.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

/**
 * 应用程序的内容既可以来源于本地静态数据（这对于演示目的很有用），也可以来源于提供最新、真实内容的产品后端服务器。
 * 这两种产品风格体现了这种行为。
 */
@Suppress("EnumEntryName")
enum class Flavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
) {
    // 演示风格，使用本地静态数据
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),

    // 产品风格，使用产品后端服务器数据
    prod(FlavorDimension.contentType),
}


fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: Flavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.entries.forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            Flavor.entries.forEach { flavor ->

                register(flavor.name) {
                    dimension = flavor.dimension.name
                    flavorConfigurationBlock(this, flavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (flavor.applicationIdSuffix != null) {
                            applicationIdSuffix = flavor.applicationIdSuffix
                        }
                    }
                }

            }
        }
    }
}
