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

package com.wujia.toolkit.jetpack.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * 分页工具类
 * @param pageSize 每页大小（默认10）
 * @param initialKey 初始键（默认1）
 * @param enablePlaceholders 是否启用占位符
 * @param loadData 数据加载函数
 */
class HiPagingHelper<Key : Any, Value : Any>(
    private val pageSize: Int = 10,
    private val initialKey: Key,
    private val enablePlaceholders: Boolean = false,
    private val loadData: suspend (Key, Int) -> Pair<List<Value>, Key?>
) {

    fun createPagingFlow(): Flow<PagingData<Value>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = enablePlaceholders
            ),
            initialKey = initialKey,
            pagingSourceFactory = {
                HiGenericPagingSource(
                    initialKey = initialKey,
                    loadSize = pageSize,
                    loadData = loadData
                )
            }
        ).flow
    }
}
