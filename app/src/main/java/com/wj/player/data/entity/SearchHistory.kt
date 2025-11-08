package com.wj.player.data.entity

/**
 * 搜索历史数据模型：仅包含关键词和时间（用于排序）
 */
data class SearchHistory(
    val keyword: String,
    val timestamp: Long = System.currentTimeMillis(), // 默认为当前时间
)
