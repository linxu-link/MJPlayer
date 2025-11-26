package com.wj.player.ui.theme.colors

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.wj.player.ui.theme.ThemeType

object Colors {
    val adaptiveThemeBg = Color(0xFFAFD4FF)
    val lightThemeBg = Color(0xFFE3EDF6)
    val darkThemeBg = Color(0xFF232323)
    val themeMark = Color(0xFF44DA87)
    val white = Color(0xFFFFFFFF)
    val light = Color(0xFFF5F5F5)
    val black = Color(0xFF000000)
    val dark = Color(0xFF151515)
    val grey = Color(0xFF2A2A2A)
    val lightGrey = Color(0xFF989898)
    val theme2 = Color(0xFFF96355)
    val theme3 = Color(0xFFFF9A56)
    val theme4 = Color(0xFF9E644E)
    val theme5 = Color(0xFFCCD518)
    val theme6 = Color(0xFF9FD951)
    val theme7 = Color(0xFF4DCD5E)
    val theme8 = Color(0xFF18C291)
    val theme9 = Color(0xFF4EB2E6)
    val theme10 = Color(0xFF528AE1)
    val theme11 = Color(0xFF5368E8)
    val theme12 = Color(0xFF9764EF)
    val theme13 = Color(0xFFDA55E4)
    val theme14 = Color(0xFFFF518C)
    val theme15 = Color(0xFFE94491)
    val theme16 = Color(0xFFDEE3E6)

}

// 自定义颜色配置类
data class MJPlayerColorScheme(
    val theme: Color,         // 主题色
    val background: Color,    // 页面背景色
    val surface: Color,       // 卡片/组件背景色
    val textPrimary: Color,   // 主要文字色
    val textPrimaryInverse: Color, // 主要文字反色
    val textSecondary: Color, // 次要文字色
    val textSecondaryInverse: Color, // 次要文字反色
    val accent: Color,        // 强调色（按钮/高亮元素）
    val accentInverse: Color,      // 强调色反色
    val border: Color,        // 边框色
)

// 黑色主题配置
val DarkColorScheme = MJPlayerColorScheme(
    theme = Colors.dark,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Colors.lightGrey,
    textSecondaryInverse = Colors.light,
    accent = Color(0xFF2196F3),
    accentInverse = Colors.light,
    border = Color(0xFF333333),
)

// 白色主题配置
val LightColorScheme = MJPlayerColorScheme(
    theme = Colors.light,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.dark,
    textPrimaryInverse = Colors.dark,
    textSecondary = Colors.lightGrey,
    textSecondaryInverse = Colors.light,
    accent = Color(0xFF2196F3),
    accentInverse = Colors.light,
    border = Color(0xFFE0E0E0),
)

val Theme2ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme2,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme2,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme3ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme3,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Colors.lightGrey,
    textSecondaryInverse = Colors.light,
    accent = Colors.theme3,
    accentInverse = Colors.light,
    border = Color(0xFF333333),
)

val Theme4ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme4,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme4,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme5ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme5,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme5,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme6ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme6,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme6,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme7ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme7,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme7,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme8ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme8,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme8,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme9ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme9,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme9,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme10ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme10,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme10,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme11ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme11,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme11,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme12ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme12,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme12,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme13ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme13,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme13,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme14ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme14,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme14,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme15ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme15,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme15,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme16ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme16,
    background = Colors.light,
    surface = Colors.light,
    textPrimary = Colors.dark,
    textPrimaryInverse = Colors.dark,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.dark,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme17ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme2,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme2,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme18ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme3,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme3,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme19ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme4,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme4,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme20ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme5,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme5,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme21ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme6,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme6,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme22ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme7,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme7,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme23ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme8,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme8,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme24ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme9,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme9,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val Theme25ColorScheme = MJPlayerColorScheme(
    theme = Colors.theme10,
    background = Colors.dark,
    surface = Colors.grey,
    textPrimary = Colors.light,
    textPrimaryInverse = Colors.light,
    textSecondary = Color(0xFF7F8C8D),
    textSecondaryInverse = Colors.light,
    accent = Colors.theme10,
    accentInverse = Colors.light,
    border = Color(0xFFBDC3C7),
)

val themeArray = mapOf(
    ThemeType.DARK to DarkColorScheme,
    ThemeType.LIGHT to LightColorScheme,
    ThemeType.THEME_2 to Theme2ColorScheme,
    ThemeType.THEME_3 to Theme3ColorScheme,
    ThemeType.THEME_4 to Theme4ColorScheme,
    ThemeType.THEME_5 to Theme5ColorScheme,
    ThemeType.THEME_6 to Theme6ColorScheme,
    ThemeType.THEME_7 to Theme7ColorScheme,
    ThemeType.THEME_8 to Theme8ColorScheme,
    ThemeType.THEME_9 to Theme9ColorScheme,
    ThemeType.THEME_10 to Theme10ColorScheme,
    ThemeType.THEME_11 to Theme11ColorScheme,
    ThemeType.THEME_12 to Theme12ColorScheme,
    ThemeType.THEME_13 to Theme13ColorScheme,
    ThemeType.THEME_14 to Theme14ColorScheme,
    ThemeType.THEME_15 to Theme15ColorScheme,
    ThemeType.THEME_16 to Theme16ColorScheme,
    ThemeType.THEME_17 to Theme17ColorScheme,
    ThemeType.THEME_18 to Theme18ColorScheme,
    ThemeType.THEME_19 to Theme19ColorScheme,
    ThemeType.THEME_20 to Theme20ColorScheme,
    ThemeType.THEME_21 to Theme21ColorScheme,
    ThemeType.THEME_22 to Theme22ColorScheme,
    ThemeType.THEME_23 to Theme23ColorScheme,
    ThemeType.THEME_24 to Theme24ColorScheme,
    ThemeType.THEME_25 to Theme25ColorScheme,
)

// 创建CompositionLocal实例，默认值为白色主题
val LocalColorScheme = compositionLocalOf<MJPlayerColorScheme> {
    // 默认颜色配置（白色主题）
    LightColorScheme
}
