package com.wj.player.ui.theme.resource

import androidx.compose.runtime.compositionLocalOf
import com.wj.player.R
import com.wj.player.ui.theme.ThemeType

object Images {
    val placeholderDark = R.drawable.ic_loading_dark
    val placeholderLight = R.drawable.ic_loading_light
    val adaptiveThemeMask = R.drawable.theme_list_system_mask
    val lightThemeMask = R.drawable.theme_list_light_mask
    val darkThemeMask = R.drawable.theme_list_dark_mask
    val blackThemeMask = R.drawable.theme_list_black_mask
}

data class ImageScheme(
    val placeholder: Int,
    val adaptiveThemeMask: Int,
    val lightThemeMask: Int,
    val darkThemeMask: Int,
)

val darkImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)

val lightImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)

val theme2ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme3ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme4ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme5ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme6ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme7ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme8ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme9ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme10ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme11ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme12ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme13ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme14ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme15ImageScheme = ImageScheme(
    placeholder = Images.placeholderLight,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme16ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.blackThemeMask,
)
val theme17ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme18ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme19ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme20ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme21ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme22ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme23ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme24ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)
val theme25ImageScheme = ImageScheme(
    placeholder = Images.placeholderDark,
    adaptiveThemeMask = Images.adaptiveThemeMask,
    lightThemeMask = Images.lightThemeMask,
    darkThemeMask = Images.darkThemeMask,
)

val themeImageArray = mapOf(
    ThemeType.DARK to darkImageScheme,
    ThemeType.LIGHT to lightImageScheme,
    ThemeType.THEME_2 to theme2ImageScheme,
    ThemeType.THEME_3 to theme3ImageScheme,
    ThemeType.THEME_4 to theme4ImageScheme,
    ThemeType.THEME_5 to theme5ImageScheme,
    ThemeType.THEME_6 to theme6ImageScheme,
    ThemeType.THEME_7 to theme7ImageScheme,
    ThemeType.THEME_8 to theme8ImageScheme,
    ThemeType.THEME_9 to theme9ImageScheme,
    ThemeType.THEME_10 to theme10ImageScheme,
    ThemeType.THEME_11 to theme11ImageScheme,
    ThemeType.THEME_12 to theme12ImageScheme,
    ThemeType.THEME_13 to theme13ImageScheme,
    ThemeType.THEME_14 to theme14ImageScheme,
    ThemeType.THEME_15 to theme15ImageScheme,
    ThemeType.THEME_16 to theme16ImageScheme,
    ThemeType.THEME_17 to theme17ImageScheme,
    ThemeType.THEME_18 to theme18ImageScheme,
    ThemeType.THEME_19 to theme19ImageScheme,
    ThemeType.THEME_20 to theme20ImageScheme,
    ThemeType.THEME_21 to theme21ImageScheme,
    ThemeType.THEME_22 to theme22ImageScheme,
    ThemeType.THEME_23 to theme23ImageScheme,
    ThemeType.THEME_24 to theme24ImageScheme,
    ThemeType.THEME_25 to theme25ImageScheme,
)

// 创建CompositionLocal实例，默认值为白色主题
val LocalImageScheme = compositionLocalOf<ImageScheme> {
    // 默认颜色配置（白色主题）
    lightImageScheme
}




