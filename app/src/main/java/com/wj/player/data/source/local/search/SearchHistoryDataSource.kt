package com.wj.player.data.source.local.search

import com.wj.player.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史数据源：处理搜索历史的本地存储（增删查）
 */
interface SearchHistoryDataSource {
    // 获取所有搜索历史（按时间倒序）
    suspend fun getSearchHistories(): Flow<List<SearchHistory>>

    // 保存搜索历史（去重+限制数量）
    suspend fun saveSearchHistory(keyword: String)

    // 删除单条搜索历史
    suspend fun deleteSearchHistory(keyword: String)

    // 清空所有搜索历史
    suspend fun clearAllSearchHistory()
}
