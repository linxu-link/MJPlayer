package com.wj.player.ui.theme.dimens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.wujia.toolkit.utils.HiLog


@Composable
fun rememberAppDimensions(): AppDimensions {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val systemDpi = displayMetrics.densityDpi // 系统标称DPI
    HiLog.e("systemDpi = $systemDpi")
    return when(systemDpi) {
        in 0..390 -> phone360DpiDimensions
        in 391..430 -> phone420DpiDimensions
        in 431..530 -> phone520DpiDimensions
        in 531..640 -> phone560DpiDimensions
        else -> phone420DpiDimensions
    }
}

// 辅助函数：保留小数位数
fun Float.format(digits: Int) = "%.${digits}f".format(this)

val LocalAppDimensions = compositionLocalOf<AppDimensions> {
    error("AppDimensions must be provided via CompositionLocalProvider")
}

