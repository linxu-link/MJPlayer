package com.wj.player.ui.theme.colors

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// 自定义颜色配置类
data class MJPlayerColorScheme(
    val theme: Color,
    val background: Color,    // 页面背景色
    val surface: Color,       // 卡片/组件背景色
    val textPrimary: Color,   // 主要文字色
    val textPrimaryInverse: Color, // 主要文字反色
    val textSecondary: Color, // 次要文字色
    val textSecondaryInverse: Color, // 次要文字反色
    val accent: Color,        // 强调色（按钮/高亮元素）
    val border: Color,         // 边框色
    )

// 黑色主题配置
val DarkColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF000000),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFFB3B3B3),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFFF4081),
    border = Color(0xFF333333),
)

// 白色主题配置
val LightColorScheme = MJPlayerColorScheme(
    theme =Color(0xFFF5F5F5),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFF000000),
    textPrimaryInverse = Color(0xFFF5F5F5),
    textSecondary = Color(0xFFB3B3B3),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFF2196F3), // 蓝色强调色
    border = Color(0xFFE0E0E0),
)

val Theme2ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFF96355),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme3ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFFF9A56),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFFB3B3B3),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFFF4081),
    border = Color(0xFF333333),
)

val Theme4ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9E644E),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme5ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFCCD518),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme6ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9FD951),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme7ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF4DCD5E),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme8ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF18C291),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme9ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF4EB2E6),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme10ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF528AE1),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme11ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF5368E8),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme12ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9764EF),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme13ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFDA55E4),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme14ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFFF518C),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme15ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFE94491),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme16ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFDEE3E6),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme17ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFF96355),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme18ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFFF9A56),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme19ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9E644E),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme20ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFFCCD518),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme21ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF9FD951),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme22ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF4DCD5E),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme23ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF18C291),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme24ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF4EB2E6),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val Theme25ColorScheme = MJPlayerColorScheme(
    theme = Color(0xFF528AE1),
    background = Color(0xFF000000),
    surface = Color(0xFF2A2A2A),
    textPrimary = Color(0xFFF5F5F5),
    textPrimaryInverse = Color(0xFF000000),
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Color(0xFFF5F5F5),
    accent = Color(0xFFE74C3C),
    border = Color(0xFFBDC3C7),
)

val themeArray = arrayOf(
    DarkColorScheme, LightColorScheme, Theme2ColorScheme,
    Theme3ColorScheme, Theme4ColorScheme, Theme5ColorScheme,
    Theme6ColorScheme, Theme7ColorScheme, Theme8ColorScheme,
    Theme9ColorScheme, Theme10ColorScheme, Theme11ColorScheme,
    Theme12ColorScheme, Theme13ColorScheme, Theme14ColorScheme,
    Theme15ColorScheme, Theme16ColorScheme, Theme17ColorScheme,
    Theme18ColorScheme, Theme19ColorScheme, Theme20ColorScheme,
    Theme21ColorScheme, Theme22ColorScheme, Theme23ColorScheme,
    Theme24ColorScheme, Theme25ColorScheme,
)

// 创建CompositionLocal实例，默认值为白色主题
val LocalColorScheme = compositionLocalOf<MJPlayerColorScheme> {
    // 默认颜色配置（白色主题）
    LightColorScheme
}

