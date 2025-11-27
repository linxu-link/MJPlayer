package com.wj.player.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.wj.player.MJConstants
import com.wj.player.arch.ArchActivity
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.theme.ThemeType
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.configuration.LocalIsLandscape
import com.wj.player.ui.theme.configuration.LocalOrientationController
import com.wj.player.ui.theme.configuration.LocalSystemUiControl
import com.wujia.toolkit.system.HiSystemBarsController
import com.wujia.toolkit.system.HiSystemBarsController.Companion.IMMERSIVE_STICKY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ArchActivity() {
    private lateinit var hiSystemUiControl: HiSystemBarsController

    private lateinit var themeListener: (ThemeType) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        hiSystemUiControl = HiSystemBarsController(this)
        // 配置 Window，让状态栏透明且内容延伸到状态栏区域
        window.apply {
            setDecorFitsSystemWindows(false)
            // 设置状态栏背景透明
            statusBarColor = Color.TRANSPARENT
            // 配置系统栏行为和外观
            decorView.windowInsetsController?.apply {
                // 系统栏行为：下拉状态栏时临时显示，滑动后自动隐藏（沉浸式常用）
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // 配置状态栏图标颜色（明暗模式）
                // Appearance.LIGHT_STATUS_BARS：深色图标（适合浅色背景）
                // 0：浅色图标（适合深色背景）
//                setSystemBarsAppearance(
//                    WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                )
                // 配置导航栏
                navigationBarColor = Color.TRANSPARENT
                setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                )
            }
        }
        super.onCreate(savedInstanceState)
        // 定义主题变更监听
        themeListener = { themeType ->
            setContent {
                MJPlayerTheme(themeType = themeType) {
                    CompositionLocalProvider(
                        LocalOrientationController provides ::toggleOrientation,
                        LocalIsLandscape provides ::isLandscape,
                        LocalSystemUiControl provides{ show ->
                            toggleFullScreen(show)
                        },
                    ) {
                        MJNaviGraph(modifier = Modifier.background(LocalColorScheme.current.background))
                    }
                }
            }
        }
        MJConstants.Theme.addThemeListener(themeListener)
    }

    private fun toggleFullScreen(show: Boolean) {
        if (show) {
            hiSystemUiControl.hideBothBars(IMMERSIVE_STICKY)
        } else {
            hiSystemUiControl.showBothBars()
        }
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
