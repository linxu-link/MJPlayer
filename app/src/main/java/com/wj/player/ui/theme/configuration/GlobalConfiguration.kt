package com.wj.player.ui.theme.configuration

import androidx.compose.runtime.compositionLocalOf
import com.wujia.toolkit.system.HiSystemBarsController

val LocalOrientationController = compositionLocalOf<() -> Unit> {
    error("LocalOrientationController must be provided via CompositionLocalProvider")
}
val LocalIsLandscape = compositionLocalOf<(() -> Boolean)> {
    error("LocalIsLandscape must be provided via CompositionLocalProvider")
}

val LocalSystemBarsController = compositionLocalOf<HiSystemBarsController> {
    error("LocalSystemBarsController must be provided via CompositionLocalProvider")
}
