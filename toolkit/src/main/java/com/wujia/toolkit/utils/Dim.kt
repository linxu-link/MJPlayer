package com.wujia.toolkit.utils

import android.content.Context
import android.util.TypedValue
import com.wujia.toolkit.HiAppGlobal

// 扩展1：Int（DP值）转为PX像素值（返回Float，适用于大多数View布局场景）
fun Int.dp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, // 单位类型：DP
        this.toFloat(), // 要转换的DP值
        HiAppGlobal.getApplication().resources.displayMetrics, // 屏幕密度信息
    )
}

// 扩展2：Int（DP值）转为PX像素值（返回Int，适用于需要整数像素的场景，如布局宽高）
fun Int.dp2pxInt(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        HiAppGlobal.getApplication().resources.displayMetrics,
    ).toInt()
}

// 反向扩展：Int（PX值）转为DP值（可选，补充完整转换逻辑）
fun Int.px2dp(): Float {
    val density = HiAppGlobal.getApplication().resources.displayMetrics.density
    return this.toFloat() / density
}
