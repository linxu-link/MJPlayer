package com.wj.player.ui.theme.colors

import androidx.annotation.IntDef

const val ADAPTIVE_THEME = -1 // 自适应主题
const val DARK_THEME = 0    // 深色主题
const val LIGHT_THEME = 1   // 浅色主题

const val THEME_2 = 2
const val THEME_3 = 3
const val THEME_4 = 4
const val THEME_5 = 5
const val THEME_6 = 6
const val THEME_7 = 7
const val THEME_8 = 8
const val THEME_9 = 9
const val THEME_10 = 10
const val THEME_11 = 11
const val THEME_12 = 12
const val THEME_13 = 13
const val THEME_14 = 14
const val THEME_15 = 15
const val THEME_16 = 16
const val THEME_17 = 17
const val THEME_18 = 18
const val THEME_19 = 19
const val THEME_20 = 20
const val THEME_21 = 21
const val THEME_22 = 22
const val THEME_23 = 23
const val THEME_24 = 24
const val THEME_25 = 25

@IntDef(
    ADAPTIVE_THEME, DARK_THEME, LIGHT_THEME, THEME_2,
    THEME_3, THEME_4, THEME_5, THEME_6, THEME_7,
    THEME_8, THEME_9, THEME_10, THEME_11, THEME_12,
    THEME_13, THEME_14, THEME_15, THEME_16, THEME_17,
    THEME_18, THEME_19, THEME_20, THEME_21, THEME_22,
    THEME_23, THEME_24, THEME_25,
)
@Retention(AnnotationRetention.SOURCE)
annotation class MJPlayerThemeType
