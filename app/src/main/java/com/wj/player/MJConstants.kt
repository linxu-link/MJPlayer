package com.wj.player

import com.wj.player.data.entity.ThemeEntity
import com.wj.player.ui.theme.colors.ADAPTIVE_THEME
import com.wj.player.ui.theme.colors.DARK_THEME
import com.wj.player.ui.theme.colors.LIGHT_THEME
import com.wj.player.ui.theme.colors.MJPlayerThemeType
import com.wj.player.ui.theme.colors.THEME_10
import com.wj.player.ui.theme.colors.THEME_11
import com.wj.player.ui.theme.colors.THEME_12
import com.wj.player.ui.theme.colors.THEME_13
import com.wj.player.ui.theme.colors.THEME_14
import com.wj.player.ui.theme.colors.THEME_15
import com.wj.player.ui.theme.colors.THEME_2
import com.wj.player.ui.theme.colors.THEME_3
import com.wj.player.ui.theme.colors.THEME_4
import com.wj.player.ui.theme.colors.THEME_5
import com.wj.player.ui.theme.colors.THEME_6
import com.wj.player.ui.theme.colors.THEME_7
import com.wj.player.ui.theme.colors.THEME_8
import com.wj.player.ui.theme.colors.THEME_9
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiSp

interface MJConstants {

    object Key {
        const val THEME_TYPE: String = "theme_type"
    }

    object Theme {

        val androidThemes = listOf(
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_adaptive),
                iconRes = R.drawable.ic_launcher,
                themeType = ADAPTIVE_THEME,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_light),
                iconRes = R.drawable.ic_launcher,
                themeType = LIGHT_THEME,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_dark),
                iconRes = R.drawable.ic_launcher,
                themeType = DARK_THEME,
            ),
        )

        val classicThemes = listOf(
            ThemeEntity(themeType = THEME_2),
            ThemeEntity(themeType = THEME_3),
            ThemeEntity(themeType = THEME_4),
            ThemeEntity(themeType = THEME_5),
            ThemeEntity(themeType = THEME_6),
            ThemeEntity(themeType = THEME_7),
            ThemeEntity(themeType = THEME_8),
            ThemeEntity(themeType = THEME_9),
            ThemeEntity(themeType = THEME_10),
            ThemeEntity(themeType = THEME_11),
            ThemeEntity(themeType = THEME_12),
            ThemeEntity(themeType = THEME_13),
            ThemeEntity(themeType = THEME_14),
            ThemeEntity(themeType = THEME_15),
        )

        val otherThemes = listOf(
            ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
            ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
            ThemeEntity("Zodiac Pisces", R.drawable.ic_launcher),
        )

        private val themeListeners = mutableListOf<(Int) -> Unit>()

        fun getThemeType() = HiSp.get(Key.THEME_TYPE, LIGHT_THEME)

        fun setThemeType(@MJPlayerThemeType themeType: Int) {
            HiSp.put(Key.THEME_TYPE, themeType)
            themeListeners.forEach { it(themeType) }
        }

        fun addThemeListener(listener: (Int) -> Unit) {
            themeListeners.add(listener)
            listener(getThemeType())
        }
    }

}
