package com.wj.player.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 视频时间工具类：将视频创建时间（毫秒）转换为“yyyy-MM-dd”格式，用于分组
 */
object VideoTimeUtils {
    private val dateDetailFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

    // 将视频创建时间（秒）转换为日期字符串（如“2024-05-20”）
    fun formatVideoDetailDate(createTimeS: Long): String {
        return dateDetailFormat.format(Date(createTimeS * 1000))
    }

    fun formatVideoSimpleDate(createTimeS: Long): String {
        return dateFormat.format(Date(createTimeS * 1000))
    }

    // 将视频时长（毫秒）转换为“mm:ss”格式（如“03:45”）
    fun formatVideoDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun formatVideoSize(sizeBytes: Long): String {
        val sizeKB = sizeBytes / 1024f
        val sizeMB = sizeKB / 1024f
        return if (sizeMB >= 1) {
            String.format("%.2f MB", sizeMB)
        } else {
            String.format("%.2f KB", sizeKB)
        }
    }
}
