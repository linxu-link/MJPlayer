package com.wj.player.domian

import com.wj.player.di.DefaultDispatcher
import com.wj.player.data.entity.Video
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "SearchVideoByKeywordUseCase"

// 1. 搜索视频用例：仅负责“获取数据+筛选”，不处理历史保存
class SearchVideoByKeywordUseCase @Inject constructor(
    private val getCachedVideosUseCase: GetCachedVideosUseCase,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(keyword: String): List<Video> = withContext(defaultDispatcher) {
        // 1. 调用仓库获取所有视频（复用现有仓库的“获取缓存/扫描本地”逻辑）
        // 注意：getAllVideosUseCase() 返回Flow，这里需要收集其数据（假设返回的是冷流，收集后才会执行获取逻辑）
        val allVideos: List<Video> = getCachedVideosUseCase().first() // 获取第一个发射的数据（全量视频列表）
        // 2. 按关键词筛选（业务逻辑：不区分大小写，标题包含关键词）
        if (keyword.isBlank()) {
            emptyList()
        } else {
            val lowerKeyword = keyword.lowercase()
            allVideos.filter { video ->
                // 标题不区分大小写包含关键词
                video.title.lowercase().contains(lowerKeyword)
            }
        }
    }
}
