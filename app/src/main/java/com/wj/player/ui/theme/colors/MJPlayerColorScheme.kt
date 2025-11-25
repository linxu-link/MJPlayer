package com.wj.player.ui.theme.colors

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// 自定义颜色配置类（替代Material3的ColorScheme）
data class MJPlayerColorScheme(
    val theme: Color,
    val background: Color,    // 页面背景色
    val surface: Color,       // 卡片/组件背景色
    val textPrimary: Color,   // 主要文字色
    val textSecondary: Color, // 次要文字色
    val accent: Color,        // 强调色（按钮/高亮元素）
    val border: Color,         // 边框色

    val white: Color = Color.White,

    )

// 黑色主题配置
val DarkColorScheme = MJPlayerColorScheme(
    theme = Color.Black,
    background = Color.Black,
    surface = Color(0xFF121212),
    textPrimary = Color.White,
    textSecondary = Color(0xFFB3B3B3),
    accent = Color(0xFFFF4081),
    border = Color(0xFF333333),
)

// 白色主题配置
val LightColorScheme = MJPlayerColorScheme(
    theme = Color.White,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    textPrimary = Color.Black,
    textSecondary = Color.Gray,
    accent = Color(0xFF2196F3), // 蓝色强调色
    border = Color(0xFFE0E0E0),
)

val Theme2ColorScheme = MJPlayerColorScheme(
    theme = Color.LightGray,
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme3ColorScheme = MJPlayerColorScheme(
    theme = Color.Red,
    background = Color.White,
    surface = Color(0xFF121212),
    textPrimary = Color.White,
    textSecondary = Color(0xFFB3B3B3),
    accent = Color(0xFFFF4081),
    border = Color(0xFF333333),
)

val Theme4ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFFF9800),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme5ColorScheme = MJPlayerColorScheme(
    theme = Color.Yellow,
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme6ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF795548),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme7ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFCDDC39),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme8ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF8BC34A),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme9ColorScheme = MJPlayerColorScheme(
    theme = Color.Green,
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme10ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF00BCD4),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme11ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF00BCD4),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme12ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF3F51B5),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme13ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF673AB7),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme14ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9C27B0),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme15ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFE91E63),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme16ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFFF4081),
    background = Color.White,
    surface = Color.White,
    textPrimary = Color.White,
    textSecondary = Color(0xFF7F8C8D),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val themeArray = arrayOf(
    DarkColorScheme, LightColorScheme, Theme2ColorScheme,
    Theme3ColorScheme, Theme4ColorScheme, Theme5ColorScheme,
    Theme6ColorScheme, Theme7ColorScheme, Theme8ColorScheme,
    Theme9ColorScheme, Theme10ColorScheme, Theme11ColorScheme,
    Theme12ColorScheme, Theme13ColorScheme, Theme14ColorScheme,
    Theme15ColorScheme, Theme16ColorScheme,
)

// 创建CompositionLocal实例，默认值为白色主题
val LocalColorScheme = compositionLocalOf<MJPlayerColorScheme> {
    // 默认颜色配置（白色主题）
    LightColorScheme
}

