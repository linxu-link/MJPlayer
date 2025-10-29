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

import com.wj.player.data.repository.video.VideoRepository
import com.wj.player.di.DefaultDispatcher
import com.wj.player.di.IODispatcher
import com.wj.player.entity.Video
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

private const val TAG = "SearchVideoByKeywordUseCase"

// 1. 搜索视频用例：仅负责“获取数据+筛选”，不处理历史保存
class SearchVideoByKeywordUseCase @Inject constructor(
    private val videoRepository: VideoRepository, // 复用现有视频仓库
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
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