package com.wj.player.data.source.local.search

import com.wj.player.entity.SearchHistory
import com.wujia.toolkit.jetpack.datastore.HiDataStoreManager
import com.wujia.toolkit.utils.gson.GsonUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

// DataStore 存储键
private const val SEARCH_HISTORY_KEY = "search_histories"

// 最大保存10条历史
private const val MAX_HISTORY_COUNT = 10

/**
 * 实现类：用 DataStore 存储，通过 JSON 序列化 List<SearchHistory>
 */
class SearchHistoryDataSourceImpl(
    private val dataStore: HiDataStoreManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SearchHistoryDataSource {

    override suspend fun getSearchHistories(): Flow<List<SearchHistory>> {
        val historyStrings = dataStore.getPreference(SEARCH_HISTORY_KEY, "").first()
        val histories: MutableList<SearchHistory> =
            GsonUtils.jsonToList(historyStrings, SearchHistory::class.java) ?: mutableListOf()
        return flowOf(histories)
    }

    override suspend fun saveSearchHistory(keyword: String) {
        withContext(ioDispatcher) {
            val trimmedKeyword = keyword.trim()
            if (trimmedKeyword.isEmpty()) {
                return@withContext // 空关键词不保存
            }
            // 1. 读取现有历史
            val existingHistories = getSearchHistories().first()
            // 2. 去重：如果关键词已存在，先移除
            val filtered = existingHistories.filter { it.keyword != trimmedKeyword }
            // 3. 添加新历史（时间为当前时间）
            val newHistories = listOf(SearchHistory(trimmedKeyword)) + filtered
            // 4. 限制数量：超过10条则截断
            val limitedHistories = newHistories.take(MAX_HISTORY_COUNT)
            // 5. 序列化为字符串集合并保存
            val historyStrings = GsonUtils.toJson(limitedHistories)
            dataStore.putPreference(SEARCH_HISTORY_KEY, historyStrings)
        }
    }

    override suspend fun deleteSearchHistory(keyword: String) {
        val existingHistories = getSearchHistories().first()
        // 过滤掉要删除的关键词
        val remaining = existingHistories.filter { it.keyword != keyword }
        val historyStrings = GsonUtils.toJson(remaining)
        dataStore.putPreference(SEARCH_HISTORY_KEY, historyStrings)
    }

    override suspend fun clearAllSearchHistory() {
        dataStore.putPreference(SEARCH_HISTORY_KEY, "")
    }
}
