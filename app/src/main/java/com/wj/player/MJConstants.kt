package com.wj.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Speed
import com.wj.player.data.entity.LAYOUT_TYPE_LIST
import com.wj.player.data.entity.LayoutType
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
import com.wj.player.ui.theme.colors.THEME_16
import com.wj.player.ui.theme.colors.THEME_17
import com.wj.player.ui.theme.colors.THEME_18
import com.wj.player.ui.theme.colors.THEME_19
import com.wj.player.ui.theme.colors.THEME_2
import com.wj.player.ui.theme.colors.THEME_20
import com.wj.player.ui.theme.colors.THEME_21
import com.wj.player.ui.theme.colors.THEME_22
import com.wj.player.ui.theme.colors.THEME_23
import com.wj.player.ui.theme.colors.THEME_24
import com.wj.player.ui.theme.colors.THEME_25
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
        const val LAYOUT_TYPE: String = "layout_type"
    }

    object Icon{
        val ARROW_BACK = Icons.AutoMirrored.Filled.ArrowBackIos
        val ARROW_UP = Icons.Default.ArrowDropUp
        val ARROW_DOWN = Icons.Default.ArrowDropDown
        val PLAYER_SPEED = Icons.Filled.Speed
    }

    object Theme {

        val androidThemes = listOf(
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_adaptive),
                iconRes = R.drawable.theme_list_system_mask,
                themeType = ADAPTIVE_THEME,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_light),
                iconRes = R.drawable.theme_list_light_mask,
                themeType = LIGHT_THEME,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_dark),
                iconRes = R.drawable.theme_list_dark_mask,
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
            ThemeEntity(themeType = THEME_16),
            ThemeEntity(themeType = THEME_17),
            ThemeEntity(themeType = THEME_18),
            ThemeEntity(themeType = THEME_19),
            ThemeEntity(themeType = THEME_20),
            ThemeEntity(themeType = THEME_21),
            ThemeEntity(themeType = THEME_22),
            ThemeEntity(themeType = THEME_23),
            ThemeEntity(themeType = THEME_24),
            ThemeEntity(themeType = THEME_25),
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

        fun getLayoutType() = HiSp.get(Key.LAYOUT_TYPE, LAYOUT_TYPE_LIST)

        fun setLayoutType(@LayoutType layoutType: Int) {
            HiSp.put(Key.LAYOUT_TYPE, layoutType)
        }
    }


}
