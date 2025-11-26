package com.wj.player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import com.wj.player.data.entity.LayoutType
import com.wj.player.data.entity.ThemeEntity
import com.wj.player.ui.theme.ThemeType
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiLog
import com.wujia.toolkit.utils.HiSp
import org.junit.runner.manipulation.Filter

interface MJConstants {

    object Key {
        const val THEME_TYPE: String = "theme_type"
        const val LAYOUT_TYPE: String = "layout_type"
    }

    object Icon {
        val ARROW_BACK = Icons.AutoMirrored.Filled.ArrowBackIos
        val ARROW_UP = Icons.Default.ArrowDropUp
        val ARROW_DOWN = Icons.Default.ArrowDropDown
        val ARROW_PLAY = Icons.Filled.PlayArrow
        val PLAYER_SPEED = Icons.Filled.Speed
        val SETTING = Icons.Filled.MoreVert
        val FILTER = Icons.Filled.FilterList
        val SEARCH = Icons.Filled.Search
        val CHECK = Icons.Filled.Check
    }

    object Theme {

        val androidThemes = listOf(
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_adaptive),
                iconRes = R.drawable.theme_list_system_mask,
                themeType = ThemeType.ADAPTIVE,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_light),
                iconRes = R.drawable.theme_list_light_mask,
                themeType = ThemeType.LIGHT,
            ),
            ThemeEntity(
                name = HiAppGlobal.getApplication().getString(R.string.theme_dark),
                iconRes = R.drawable.theme_list_dark_mask,
                themeType = ThemeType.DARK,
            ),
        )

        val classicThemes = listOf(
            ThemeEntity(themeType = ThemeType.THEME_2),
            ThemeEntity(themeType = ThemeType.THEME_3),
            ThemeEntity(themeType = ThemeType.THEME_4),
            ThemeEntity(themeType = ThemeType.THEME_5),
            ThemeEntity(themeType = ThemeType.THEME_6),
            ThemeEntity(themeType = ThemeType.THEME_7),
            ThemeEntity(themeType = ThemeType.THEME_8),
            ThemeEntity(themeType = ThemeType.THEME_9),
            ThemeEntity(themeType = ThemeType.THEME_10),
            ThemeEntity(themeType = ThemeType.THEME_11),
            ThemeEntity(themeType = ThemeType.THEME_12),
            ThemeEntity(themeType = ThemeType.THEME_13),
            ThemeEntity(themeType = ThemeType.THEME_14),
            ThemeEntity(themeType = ThemeType.THEME_15),
            ThemeEntity(themeType = ThemeType.THEME_16),
            ThemeEntity(themeType = ThemeType.THEME_17),
            ThemeEntity(themeType = ThemeType.THEME_18),
            ThemeEntity(themeType = ThemeType.THEME_19),
            ThemeEntity(themeType = ThemeType.THEME_20),
            ThemeEntity(themeType = ThemeType.THEME_21),
            ThemeEntity(themeType = ThemeType.THEME_22),
            ThemeEntity(themeType = ThemeType.THEME_23),
            ThemeEntity(themeType = ThemeType.THEME_24),
            ThemeEntity(themeType = ThemeType.THEME_25),
        )

        val otherThemes = listOf(
            ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
            ThemeEntity("Zodiac Aries", R.drawable.ic_launcher),
            ThemeEntity("Zodiac Pisces", R.drawable.ic_launcher),
        )

        private val themeListeners = mutableListOf<(ThemeType) -> Unit>()

        fun getThemeType(): ThemeType {
            return ThemeType.valueOf(HiSp.get(Key.THEME_TYPE, ThemeType.ADAPTIVE.name))
        }

        fun setThemeType(themeType: ThemeType) {
            HiSp.put(Key.THEME_TYPE, themeType.name)
            themeListeners.forEach { it(themeType) }
        }

        fun addThemeListener(listener: (ThemeType) -> Unit) {
            themeListeners.add(listener)
            listener(getThemeType())
        }

        fun getLayoutType() = HiSp.get(Key.LAYOUT_TYPE, LayoutType.LIST.name)

        fun setLayoutType(layoutType: LayoutType) {
            HiSp.put(Key.LAYOUT_TYPE, layoutType.name)
        }
    }


}
