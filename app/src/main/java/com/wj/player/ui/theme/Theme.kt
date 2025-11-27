package com.wj.player.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.wj.player.ui.theme.colors.darkColorScheme
import com.wj.player.ui.theme.colors.lightColorScheme
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.colors.themeColorArray
import com.wj.player.ui.theme.dimens.LocalAppDimensions
import com.wj.player.ui.theme.dimens.rememberAppDimensions
import com.wj.player.ui.theme.resource.LocalImageScheme
import com.wj.player.ui.theme.resource.darkImageScheme
import com.wj.player.ui.theme.resource.lightImageScheme
import com.wj.player.ui.theme.resource.themeImageArray
import com.wj.player.ui.theme.textstyle.LocalTypography

enum class ThemeType {
    ADAPTIVE, DARK, LIGHT, THEME_2, THEME_3, THEME_4, THEME_5, THEME_6, THEME_7, THEME_8, THEME_9,
    THEME_10, THEME_11, THEME_12, THEME_13, THEME_14, THEME_15, THEME_16, THEME_17, THEME_18,
    THEME_19, THEME_20, THEME_21, THEME_22, THEME_23, THEME_24, THEME_25,
}

@Composable
fun MJPlayerTheme(
    themeType: ThemeType = ThemeType.ADAPTIVE,
    content: @Composable () -> Unit,
) {

    val dimensions = rememberAppDimensions()

    val colorScheme = if (ThemeType.ADAPTIVE == themeType) {
        if (isSystemInDarkTheme()) {
            darkColorScheme
        } else {
            lightColorScheme
        }
    } else {
        themeColorArray[themeType] ?: lightColorScheme
    }

    val imageScheme = if (ThemeType.ADAPTIVE == themeType) {
        if (isSystemInDarkTheme()) {
            darkImageScheme
        } else {
            lightImageScheme
        }
    } else {
        themeImageArray[themeType] ?: lightImageScheme
    }

    CompositionLocalProvider(
        LocalAppDimensions provides dimensions,
        LocalColorScheme provides colorScheme,
        LocalImageScheme provides imageScheme,
        LocalTypography provides LocalTypography.current,
        content = content,
    )

}
