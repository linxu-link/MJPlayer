package com.wj.player.data.entity

import androidx.compose.ui.graphics.Color
import com.wj.player.ui.theme.colors.ADAPTIVE_THEME
import com.wj.player.ui.theme.colors.THEME_17
import com.wj.player.ui.theme.colors.THEME_18
import com.wj.player.ui.theme.colors.THEME_19
import com.wj.player.ui.theme.colors.THEME_20
import com.wj.player.ui.theme.colors.THEME_21
import com.wj.player.ui.theme.colors.THEME_22
import com.wj.player.ui.theme.colors.THEME_23
import com.wj.player.ui.theme.colors.THEME_24
import com.wj.player.ui.theme.colors.THEME_25
import com.wj.player.ui.theme.colors.Theme17ColorScheme
import com.wj.player.ui.theme.colors.Theme18ColorScheme
import com.wj.player.ui.theme.colors.Theme19ColorScheme
import com.wj.player.ui.theme.colors.Theme20ColorScheme
import com.wj.player.ui.theme.colors.Theme21ColorScheme
import com.wj.player.ui.theme.colors.Theme22ColorScheme
import com.wj.player.ui.theme.colors.Theme23ColorScheme
import com.wj.player.ui.theme.colors.Theme24ColorScheme
import com.wj.player.ui.theme.colors.Theme25ColorScheme
import com.wj.player.ui.theme.colors.themeArray

// 经典主题（色块）
data class ThemeEntity(
    val name: String = "",
    val iconRes: Int = 0,
    val themeType: Int = ADAPTIVE_THEME,
    val isSelected: Boolean = false,
)

// 是否是渐变色
fun ThemeEntity.isGradient() = themeType in THEME_17..THEME_25

fun ThemeEntity.getColor(): Color {
    return themeArray[themeType].theme
}

fun ThemeEntity.getGradientColors(): List<Color> = when (themeType) {
    THEME_17 -> listOf(Theme17ColorScheme.theme, Color(0xFF312F3C))
    THEME_18 -> listOf(Theme18ColorScheme.theme, Color(0xFF312F3C))
    THEME_19 -> listOf(Theme19ColorScheme.theme, Color(0xFF312F3C))
    THEME_20 -> listOf(Theme20ColorScheme.theme, Color(0xFF312F3C))
    THEME_21 -> listOf(Theme21ColorScheme.theme, Color(0xFF312F3C))
    THEME_22 -> listOf(Theme22ColorScheme.theme, Color(0xFF312F3C))
    THEME_23 -> listOf(Theme23ColorScheme.theme, Color(0xFF312F3C))
    THEME_24 -> listOf(Theme24ColorScheme.theme, Color(0xFF312F3C))
    THEME_25 -> listOf(Theme25ColorScheme.theme, Color(0xFF312F3C))
    else -> emptyList()
}
