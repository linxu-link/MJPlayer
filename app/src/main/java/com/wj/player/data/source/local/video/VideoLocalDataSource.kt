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

package com.wj.player.data.source.local.video

import android.content.Context
import androidx.paging.PagingSource
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.entity.Video
import kotlinx.coroutines.flow.Flow

/**
 * 视频本地数据源接口：定义“扫描本地视频”和“操作本地缓存”的契约
 */
interface VideoLocalDataSource {

    // 1. 获取Paging3数据源（用于分页加载）
    fun getPagingVideos(): PagingSource<Int, VideoEntity>

    // 2. 同步MediaStore视频到Room（首次加载/刷新时调用）
    suspend fun syncMediaStoreToRoom(context: Context)

    // 3. 监听视频变化（返回Flow，数据变化时自动发射新数据）
    fun observeVideoChanges(): Flow<Unit>

    /**
     * 从本地缓存（Room）获取视频列表
     * @return 缓存的视频列表（Flow 类型，支持实时更新）
     */
    fun getCachedVideos(): Flow<List<Video>>

    /**
     * 清空本地视频缓存
     */
    suspend fun clearVideoCache()

    suspend fun getLatestCacheTime(): Long
}