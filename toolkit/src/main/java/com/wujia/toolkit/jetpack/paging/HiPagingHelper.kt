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
    private val loadData: suspend (Key, Int) -> Pair<List<Value>, Key?>,
) {

    fun createPagingFlow(): Flow<PagingData<Value>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = enablePlaceholders,
            ),
            initialKey = initialKey,
            pagingSourceFactory = {
                HiGenericPagingSource(
                    initialKey = initialKey,
                    loadSize = pageSize,
                    loadData = loadData,
                )
            },
        ).flow
    }
}
