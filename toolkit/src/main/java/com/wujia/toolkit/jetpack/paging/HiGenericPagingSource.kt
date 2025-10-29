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


import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * 通用分页数据源封装
 * @param Key 分页键类型（通常为Int/String）
 * @param Value 数据实体类型
 * @param initialKey 初始页码/键
 * @param loadSize 每页加载数量
 * @param loadData 数据加载函数（返回Pair<数据列表,下一页键>）
 */
open class HiGenericPagingSource<Key : Any, Value : Any>(
    private val initialKey: Key,
    private val loadSize: Int = 10,
    private val loadData: suspend (Key, Int) -> Pair<List<Value>, Key?>
) : PagingSource<Key, Value>() {

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        return try {
            val currentKey = params.key ?: initialKey
            val (items, nextKey) = loadData(currentKey, loadSize)
            LoadResult.Page(
                data = items,
                prevKey = null, // 仅支持向前分页
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Key, Value>): Key? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey ?: initialKey
        }
    }
}
