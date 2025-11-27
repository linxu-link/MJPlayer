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

// ui/theme/Theme.kt
// 主题类型
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

    // 根据主题类型选择颜色方案
    val colorScheme = if (ThemeType.ADAPTIVE == themeType) {
        if (isSystemInDarkTheme()) {
            darkColorScheme
        } else {
            lightColorScheme
        }
    } else {
        themeColorArray[themeType] ?: lightColorScheme
    }

    // 根据主题类型选择图标方案
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
        LocalColorScheme provides colorScheme,
        LocalImageScheme provides imageScheme,
        content = content,
    )

}
