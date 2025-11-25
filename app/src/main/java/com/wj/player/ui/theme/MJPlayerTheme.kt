package com.wj.player.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.wj.player.ui.theme.colors.ADAPTIVE_THEME
import com.wj.player.ui.theme.colors.DarkColorScheme
import com.wj.player.ui.theme.colors.LIGHT_THEME
import com.wj.player.ui.theme.colors.LightColorScheme
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.colors.MJPlayerThemeType
import com.wj.player.ui.theme.colors.themeArray
import com.wj.player.ui.theme.dimens.LocalAppDimensions
import com.wj.player.ui.theme.dimens.rememberAppDimensions
import com.wj.player.ui.theme.size.LocalTypography


@Composable
fun MJPlayerTheme(
    @MJPlayerThemeType
    themeType: Int = LIGHT_THEME,
    content: @Composable () -> Unit,
) {

    val dimensions = rememberAppDimensions()

    val colorScheme = if (ADAPTIVE_THEME == themeType) {
        if (isSystemInDarkTheme()) {
            DarkColorScheme
        } else {
            LightColorScheme
        }
    } else {
        themeArray[themeType]
    }

    CompositionLocalProvider(
        LocalAppDimensions provides dimensions,
        LocalColorScheme provides colorScheme,
        LocalTypography provides LocalTypography.current,
        content = content,
    )

}
