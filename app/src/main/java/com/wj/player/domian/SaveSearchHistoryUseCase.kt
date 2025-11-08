package com.wj.player.domian

import com.wj.player.data.repository.search.SearchHistoryRepository
import com.wj.player.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// 2. 保存搜索历史用例：仅负责“验证+保存”，不处理搜索逻辑
class SaveSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(keyword: String, hasMatchedResult: Boolean): Boolean =
        withContext(defaultDispatcher) {
            val trimmedKeyword = keyword.trim()
            // 业务规则：关键词非空且有匹配结果时才保存
            if (trimmedKeyword.isEmpty() || !hasMatchedResult) {
                return@withContext false
            }
            // 调用历史仓库保存（复用现有仓库能力）
            searchHistoryRepository.saveSearchHistory(trimmedKeyword)
            return@withContext true
        }
}
