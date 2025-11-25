package com.wj.player.data.entity

import androidx.compose.ui.graphics.Color
import com.wj.player.ui.theme.colors.ADAPTIVE_THEME
import com.wj.player.ui.theme.colors.THEME_10
import com.wj.player.ui.theme.colors.THEME_20
import com.wj.player.ui.theme.colors.themeArray

// 经典主题（色块）
data class ThemeEntity(
    val name: String = "",
    val iconRes: Int = 0,
    val themeType: Int = ADAPTIVE_THEME,
    val isSelected: Boolean = false,
)

// 是否是渐变色
fun ThemeEntity.isGradient() = themeType in THEME_10..THEME_20

fun ThemeEntity.getColor(): Color {
    return themeArray[themeType].theme
}

fun ThemeEntity.getGradientColors(): List<Color> = when (themeType) {
    THEME_10 -> listOf(Color(0xFF00C6FF), Color(0xFF007AFF))
    THEME_20 -> listOf(Color(0xFF00C6FF), Color(0xFF007AFF))
    else -> emptyList()
}
