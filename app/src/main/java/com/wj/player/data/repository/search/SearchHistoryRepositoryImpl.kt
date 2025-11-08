package com.wj.player.data.repository.search

import com.wj.player.data.source.local.search.SearchHistoryDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepositoryImpl @Inject constructor(
    private val dataSource: SearchHistoryDataSource,
) : SearchHistoryRepository {

    override suspend fun getSearchHistories() = dataSource.getSearchHistories()
    override suspend fun saveSearchHistory(keyword: String) = dataSource.saveSearchHistory(keyword)
    override suspend fun deleteSearchHistory(keyword: String) =
        dataSource.deleteSearchHistory(keyword)

    override suspend fun clearAllSearchHistory() = dataSource.clearAllSearchHistory()
}
