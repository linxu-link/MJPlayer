package com.wj.player.ui.theme.dimens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp


@Composable
fun rememberAppDimensions(): AppDimensions {
    // 读取屏幕宽度（全局适配）
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    return when {
        screenWidth >= 720.dp -> LargeTabletDimensions
        screenWidth >= 600.dp -> TabletDimensions
        else -> PhoneDimensions
    }
}

val LocalAppDimensions = compositionLocalOf<AppDimensions> {
    error("AppDimensions must be provided via CompositionLocalProvider")
}
