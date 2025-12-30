package com.wj.player.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wj.player.MJConstants
import com.wj.player.arch.ArchActivity
import com.wj.player.ui.theme.MJPlayerTheme
import com.wj.player.ui.theme.ThemeType
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.configuration.LocalIsLandscape
import com.wj.player.ui.theme.configuration.LocalOrientationController
import com.wj.player.ui.theme.configuration.LocalSystemBarsController
import com.wujia.toolkit.system.HiSystemBarsController
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ArchActivity() {

    private val viewModel by viewModels<MainViewModel>()

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
                        // 2. 观察ViewModel中的视频Uri状态
                        val videoUri by viewModel.videoUri.collectAsState()

                        MJNaviGraph(
                            modifier = Modifier.background(LocalColorScheme.current.background),
                            videoUri = videoUri,
                        )
                    }
                }
            }
        }
        MJConstants.Theme.addThemeListener(themeListener)
        handleIntent(intent)
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

    // 若Activity启动模式为singleTop等，需重写onNewIntent处理重复唤起
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    /**
     * 处理传递过来的Intent，获取视频Uri
     */
    private fun handleIntent(intent: Intent?) {
        if (intent == null || Intent.ACTION_VIEW != intent.action) {
            return
        }
        // 获取视频文件Uri（可能是file://或content://格式）
        val videoUri = intent.data
        HiLog.e("handleIntent: videoUri = $videoUri")
        if (videoUri == null) {
            return
        }
        viewModel.setVideoUri(videoUri)
    }

}
