package com.wj.player.domian

import com.wj.player.data.entity.Video
import com.wj.player.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchVideoByIdUseCase @Inject constructor(
    private val getCachedVideosUseCase: GetCachedVideosUseCase,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(videoId: Long): Video? = withContext(defaultDispatcher) {
        // 1. 调用仓库获取所有视频（复用现有仓库的“获取缓存/扫描本地”逻辑）
        // 注意：getAllVideosUseCase() 返回Flow，这里需要收集其数据（假设返回的是冷流，收集后才会执行获取逻辑）
        val allVideos: List<Video> = getCachedVideosUseCase().first() // 获取第一个发射的数据（全量视频列表）
        // 2. 按视频ID筛选（业务逻辑：ID匹配）
        allVideos.find { video ->
            video.id == videoId
        }
    }
}
