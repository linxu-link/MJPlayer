/*
 * Copyright 2025 WuJia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private val defaultDispatcher: CoroutineDispatcher
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