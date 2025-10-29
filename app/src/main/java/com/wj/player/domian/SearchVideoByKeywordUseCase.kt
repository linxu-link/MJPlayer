package com.wj.player.domian

import com.wj.player.data.repository.video.VideoRepository
import com.wj.player.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

private const val TAG = "SearchVideoByKeywordUseCase"

// 1. 搜索视频用例：仅负责“获取数据+筛选”，不处理历史保存
class SearchVideoByKeywordUseCase @Inject constructor(
    private val videoRepository: VideoRepository, // 复用现有视频仓库
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
) {

    // 用 operator invoke 让用例可像函数一样调用（官方推荐）
//    suspend operator fun invoke(keyword: String): List<Video> = withContext(defaultDispatcher) {
//        // 1. 调用仓库获取所有视频（复用现有仓库的“获取缓存/扫描本地”逻辑）
//        val allVideos = videoRepository.getVideos(forceRefresh = true).first() // 假设仓库已有获取缓存的方法
//        // 2. 按关键词筛选（业务逻辑：不区分大小写，标题包含关键词）
//        allVideos.filter { video ->
//            video.title.lowercase(Locale.CHINA).contains(keyword.lowercase(Locale.CHINA))
//        }.also {
//
//        }
//    }
}
