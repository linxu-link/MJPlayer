package com.wj.player.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.wj.player.MJConstants
import com.wj.player.arch.ArchActivity
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.configuration.LocalIsLandscape
import com.wj.player.ui.theme.configuration.LocalOrientationController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ArchActivity() {

    private var currentTheme = MJConstants.Theme.getThemeType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MJConstants.Theme.addThemeListener { themeType ->
            currentTheme = themeType
            setContent {
                MJPlayerTheme(themeType = currentTheme) {
                    CompositionLocalProvider(
                        LocalOrientationController provides ::toggleOrientation,
                        LocalIsLandscape provides ::isLandscape,
                    ) {
                        MJNaviGraph(modifier = Modifier.background(LocalColorScheme.current.background))
                    }
                }
            }
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
}
