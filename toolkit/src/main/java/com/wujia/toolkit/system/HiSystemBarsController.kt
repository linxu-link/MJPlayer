package com.wujia.toolkit.system

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.wujia.toolkit.HiAppGlobal

/**
 * 系统栏控制器：统一管理状态栏 & 导航栏的显示、隐藏、样式
 */
class HiSystemBarsController constructor(
    private val activity: Activity,
) {

    private val window: Window = activity.window
    private val decorView: View = window.decorView
    private val insetsController: WindowInsetsControllerCompat =
        WindowInsetsControllerCompat(window, decorView)

    fun getStatusBarHeight(): Int {
        val context = HiAppGlobal.getApplication()
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getNavigationBarHeight(): Int {
        val context = HiAppGlobal.getApplication()
        var result = 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar() {
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
    }

    /**
     * 隐藏导航栏
     */
    fun hideNavigationBar() {
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }

    /**
     * 隐藏系统栏（状态栏 + 导航栏）
     */
    fun hideSystemBars() {
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * 显示状态栏
     */
    fun showStatusBar() {
        insetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    /**
     * 显示导航栏
     */
    fun showNavigationBar() {
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    /**
     * 显示系统栏
     */
    fun showSystemBars() {
        insetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    /**
     * 设置沉浸式行为
     * @param sticky 是否为“粘性”沉浸（滑动边缘可临时呼出）
     */
    fun setImmersive(sticky: Boolean = true) {
        if (sticky) {
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }

    /**
     * 设置状态栏图标/文字为浅色（白）或深色（黑）
     * - true: 浅色图标（适合深色背景）
     * - false: 深色图标（适合浅色背景）
     */
    var isStatusBarLight: Boolean
        get() = insetsController.isAppearanceLightStatusBars
        set(value) {
            insetsController.isAppearanceLightStatusBars = value
        }

    /**
     * 设置导航栏按钮为浅色或深色
     */
    var isNavigationBarLight: Boolean
        get() = insetsController.isAppearanceLightNavigationBars
        set(value) {
            insetsController.isAppearanceLightNavigationBars = value
        }

    /**
     * 设置状态栏背景色（需配合 setDecorFitsSystemWindows(false) 使用）
     */
    fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }

    /**
     * 设置导航栏背景色
     */
    fun setNavigationBarColor(color: Int) {
        window.navigationBarColor = color
    }

    /**
     * 设置系统栏完全透明
     */
    fun setSystemBarsTransparent() {
        setStatusBarColor(Color.TRANSPARENT)
        setNavigationBarColor(Color.TRANSPARENT)
    }

    /**
     * 控制内容是否延伸到系统栏区域
     * - true: 内容不延伸（默认行为，系统栏占空间）
     * - false: 内容可延伸（需手动处理 padding）
     */
    fun setDecorFitsSystemWindows(fit: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, fit)
    }

    fun setSystemBarsVisible(visible: Boolean) {
        if (visible) {
            hideSystemBars()
        } else {
            showSystemBars()
        }
    }
}
