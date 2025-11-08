package com.wj.player.domian

import com.wj.player.data.repository.search.SearchHistoryRepository
import com.wj.player.di.DefaultDispatcher
import com.wj.player.data.entity.SearchHistory
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 获取搜索历史用例：仅负责“获取”，不处理业务逻辑
 */
class GetSearchHistoriesUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(): Flow<List<SearchHistory>> = withContext(defaultDispatcher) {
        HiLog.e("GetSearchHistoriesUseCase invoke")
        return@withContext historyRepository.getSearchHistories()
    }

}
