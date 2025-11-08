package com.wj.player.domian

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wj.player.data.repository.video.VideoRepository
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPagingVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
    @IODispatcher
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    // 1. 获取Paging3数据流（供UI层观察）
    operator fun invoke(): Flow<PagingData<VideoEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = 20, // 每页加载20条
                prefetchDistance = 5, // 距离底部5条时预加载下一页
                enablePlaceholders = false, // 不显示占位符
            ),
        ) {
            videoRepository.getPagingVideos()
        }.flow
            .flowOn(coroutineDispatcher) // 分页加载在IO线程执行

    // 2. 同步本地视频（首次加载/手动刷新时调用）
    suspend fun syncLocalVideos(context: Context) =
        videoRepository.syncLocalVideos(context)

    // 3. 监听视频变化（供UI层观察，自动刷新）
    fun observeVideoChanges(): Flow<Unit> =
        videoRepository.observeVideoChanges()
}
