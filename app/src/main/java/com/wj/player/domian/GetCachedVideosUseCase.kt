package com.wj.player.domian

import com.wj.player.data.repository.video.VideoRepository
import com.wj.player.di.DefaultDispatcher
import com.wj.player.data.entity.Video
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取缓存视频用例：仅负责“获取”，不处理业务逻辑
 */
class GetCachedVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    operator fun invoke(): Flow<List<Video>> {
       return videoRepository.getVideos(true)
    }

}
