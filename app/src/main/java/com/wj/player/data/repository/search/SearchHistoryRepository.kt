package com.wj.player.data.repository.search

import com.wj.player.entity.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史仓库：协调数据源，提供简洁接口给上层
 */
interface SearchHistoryRepository {
    suspend fun getSearchHistories(): Flow<List<SearchHistory>>
    suspend fun saveSearchHistory(keyword: String)
    suspend fun deleteSearchHistory(keyword: String)
    suspend fun clearAllSearchHistory()
}
