package com.wj.player.domian

import com.wj.player.data.repository.search.SearchHistoryRepository
import com.wj.player.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearSearchHistoryUseCase @Inject constructor(
    private val historyRepository: SearchHistoryRepository,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke() = withContext(defaultDispatcher) {
        historyRepository.clearAllSearchHistory()
    }

}
