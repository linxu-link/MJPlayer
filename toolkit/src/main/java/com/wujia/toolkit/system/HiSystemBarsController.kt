package com.wujia.toolkit.system

import android.app.Activity
import android.view.WindowInsetsController
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import com.wujia.toolkit.HiAppGlobal

/**
 * 系统栏控制工具类 (Kotlin)
 * 支持 Android 6.0 (API 23) 及以上版本
 * 针对 Android 11 (API 30) 及以上版本使用新 API
 * 功能：控制状态栏和导航栏的显示/隐藏，监听系统栏的显示状态变化
 */
class HiSystemBarsController(private val activity: Activity) {

    private val window: Window = activity.window
    private var visibilityChangeListener: OnSystemBarsVisibilityChangeListener? = null

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(): Int {
        val context = HiAppGlobal.getApplication()
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideStatusBarAndroid11Plus()
        } else {
            hideStatusBarLegacy()
        }
    }

    /**
     * 显示状态栏
     */
    fun showStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            showStatusBarAndroid11Plus()
        } else {
            showStatusBarLegacy()
        }
    }

    /**
     * 隐藏导航栏
     */
    fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideNavigationBarAndroid11Plus()
        } else {
            hideNavigationBarLegacy()
        }
    }

    /**
     * 显示导航栏
     */
    fun showNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            showNavigationBarAndroid11Plus()
        } else {
            showNavigationBarLegacy()
        }
    }

    /**
     * 同时隐藏状态栏和导航栏（沉浸式模式）
     * @param immersiveMode 沉浸模式类型：IMMERSIVE、IMMERSIVE_STICKY 或 0（默认隐藏）
     */
    fun hideBothBars(immersiveMode: Int = 0) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
                hideBothBarsAndroid11Plus(immersiveMode)

            else ->
                hideBothBarsLegacy(immersiveMode)
        }
    }

    /**
     * 同时显示状态栏和导航栏
     */
    fun showBothBars() {
        showStatusBar()
        showNavigationBar()
    }

    /**
     * 设置系统栏 visibility 变化监听器
     * @param listener 监听器
     */
    fun setOnSystemBarsVisibilityChangeListener(listener: OnSystemBarsVisibilityChangeListener) {
        this.visibilityChangeListener = listener
        setupSystemUiVisibilityListener()
    }

    /**
     * 启用布局延伸到系统栏区域
     * 并设置稳定布局，防止系统栏显示/隐藏时布局 resize
     */
    fun enableLayoutBehindSystemBars() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    /**
     * 检查状态栏当前是否可见
     */
    fun isStatusBarVisible(): Boolean {
        return (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0
    }

    /**
     * 检查导航栏当前是否可见
     */
    fun isNavigationBarVisible(): Boolean {
        return (window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
    }

    /* Android 11 (API 30) 及以上版本的新 API 实现 */

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideStatusBarAndroid11Plus() {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showStatusBarAndroid11Plus() {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.statusBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBarAndroid11Plus() {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showNavigationBarAndroid11Plus() {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.navigationBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideBothBarsAndroid11Plus(immersiveMode: Int) {
        window.setDecorFitsSystemWindows(false)
        val controller = window.insetsController ?: return

        when (immersiveMode) {
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY ->
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            else ->
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
        }
        controller.hide(WindowInsets.Type.systemBars())
    }

    /* Android 6.0 (API 23) 至 Android 10 (API 29) 的传统实现 */

    private fun hideStatusBarLegacy() {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun showStatusBarLegacy() {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
    }

    private fun hideNavigationBarLegacy() {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun showNavigationBarLegacy() {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
    }

    private fun hideBothBarsLegacy(immersiveMode: Int) {
        var uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        // 添加沉浸模式标志（Android 4.4+）
        if (immersiveMode != 0) {
            uiOptions = uiOptions or immersiveMode
        }

        window.decorView.systemUiVisibility = uiOptions
    }

    /* 系统栏 visibility 变化监听 */

    private fun setupSystemUiVisibilityListener() {
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            visibilityChangeListener?.onVisibilityChanged(
                isStatusBarVisible = (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0,
                isNavigationBarVisible = (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0,
            )
        }

        // Android 11+ 可以使用 WindowInsets 监听，这里简化实现
        // 实际项目中可以根据需要添加 WindowInsets 监听
    }

    /**
     * 系统栏 visibility 变化监听接口
     */
    fun interface OnSystemBarsVisibilityChangeListener {
        fun onVisibilityChanged(isStatusBarVisible: Boolean, isNavigationBarVisible: Boolean)
    }

    companion object {
        // 沉浸模式常量
        const val IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
        const val IMMERSIVE_STICKY = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}
