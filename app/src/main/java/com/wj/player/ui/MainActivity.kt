package com.wj.player.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.wj.player.MJConstants
import com.wj.player.arch.ArchActivity
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.theme.ThemeType
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.configuration.LocalIsLandscape
import com.wj.player.ui.theme.configuration.LocalOrientationController
import com.wj.player.ui.theme.configuration.LocalSystemBarsController
import com.wujia.toolkit.system.HiSystemBarsController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ArchActivity() {
    private lateinit var themeListener: (ThemeType) -> Unit
    private val systemBars by lazy { HiSystemBarsController(activity = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        systemBars.setDecorFitsSystemWindows(false)
        systemBars.setNavigationBarColor(Color.TRANSPARENT)
        systemBars.setStatusBarColor(Color.TRANSPARENT)
        super.onCreate(savedInstanceState)
        // 主题变更监听
        themeListener = { themeType ->
            setContent {
                MJPlayerTheme(themeType = themeType) {
                    // 根据主题类型设置系统栏颜色
                    if (ThemeType.ADAPTIVE == themeType) {
                        if (isSystemInDarkTheme()) {
                            systemBars.isStatusBarLight = false
                            systemBars.isNavigationBarLight = false
                        } else {
                            systemBars.isStatusBarLight = true
                            systemBars.isNavigationBarLight = true
                        }
                    } else if (themeType == ThemeType.DARK) {
                        systemBars.isStatusBarLight = false
                        systemBars.isNavigationBarLight = false
                    } else if (themeType in ThemeType.LIGHT..ThemeType.THEME_16) {
                        systemBars.isStatusBarLight = true
                        systemBars.isNavigationBarLight = true
                    } else if (themeType in ThemeType.THEME_17..ThemeType.THEME_25) {
                        systemBars.isStatusBarLight = true
                        systemBars.isNavigationBarLight = false
                    }

                    CompositionLocalProvider(
                        LocalOrientationController provides ::toggleOrientation,
                        LocalIsLandscape provides ::isLandscape,
                        LocalSystemBarsController provides systemBars,
                    ) {
                        MJNaviGraph(modifier = Modifier.background(LocalColorScheme.current.background))
                    }
                }
            }
        }
        MJConstants.Theme.addThemeListener(themeListener)
    }

    // 屏幕方向切换方法（基于系统配置）
    private fun toggleOrientation() {
        // 从系统配置获取当前方向
        val currentOrientation = resources.configuration.orientation
        // 根据当前方向切换到相反方向
        requestedOrientation = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun isLandscape() =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    override fun onDestroy() {
        super.onDestroy()
        MJConstants.Theme.addThemeListener(themeListener)
    }
}
