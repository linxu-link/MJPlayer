package com.wj.player.ui.theme.configuration

import androidx.compose.runtime.compositionLocalOf

val LocalOrientationController = compositionLocalOf<() -> Unit> {
    error("LocalOrientationController must be provided via CompositionLocalProvider")
}

val LocalIsLandscape = compositionLocalOf<(() -> Boolean)> {
    error("LocalIsLandscape must be provided via CompositionLocalProvider")
}
