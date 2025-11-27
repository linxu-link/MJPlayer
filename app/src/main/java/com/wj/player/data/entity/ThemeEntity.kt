package com.wj.player.data.entity

import androidx.compose.ui.graphics.Color
import com.wj.player.ui.theme.ThemeType
import com.wj.player.ui.theme.colors.theme17ColorScheme
import com.wj.player.ui.theme.colors.theme18ColorScheme
import com.wj.player.ui.theme.colors.theme19ColorScheme
import com.wj.player.ui.theme.colors.theme20ColorScheme
import com.wj.player.ui.theme.colors.theme21ColorScheme
import com.wj.player.ui.theme.colors.theme22ColorScheme
import com.wj.player.ui.theme.colors.theme23ColorScheme
import com.wj.player.ui.theme.colors.theme24ColorScheme
import com.wj.player.ui.theme.colors.theme25ColorScheme
import com.wj.player.ui.theme.colors.themeColorArray

// 经典主题（色块）
data class ThemeEntity(
    val name: String = "",
    val iconRes: Int = 0,
    val themeType: ThemeType = ThemeType.ADAPTIVE,
    val isSelected: Boolean = false,
)

// 是否是渐变色
fun ThemeEntity.isGradient() = themeType in ThemeType.THEME_17..ThemeType.THEME_25

fun ThemeEntity.getColor(): Color {
    return themeColorArray[themeType]?.theme ?: Color.Unspecified
}

fun ThemeEntity.getGradientColors(): List<Color> = when (themeType) {
    ThemeType.THEME_17 -> listOf(theme17ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_18 -> listOf(theme18ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_19 -> listOf(theme19ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_20 -> listOf(theme20ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_21 -> listOf(theme21ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_22 -> listOf(theme22ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_23 -> listOf(theme23ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_24 -> listOf(theme24ColorScheme.theme, Color(0xFF312F3C))
    ThemeType.THEME_25 -> listOf(theme25ColorScheme.theme, Color(0xFF312F3C))
    else -> emptyList()
}
