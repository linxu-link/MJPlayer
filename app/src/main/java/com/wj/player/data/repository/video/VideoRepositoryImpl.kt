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

package com.wj.player.data.repository.video

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import com.wj.player.data.source.local.video.VideoLocalDataSource
import com.wj.player.data.source.local.video.room.VideoDatabase
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wujia.toolkit.HiAppGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 视频仓库实现类：调度 LocalDataSource，处理缓存策略和错误
 */
@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val localDataSource: VideoLocalDataSource
) : VideoRepository {

    // 扫描状态：用 MutableStateFlow 存储，对外暴露不可变的 StateFlow
    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    override fun getScanState(): StateFlow<ScanState> = _scanState

    // 缓存过期时间：1小时（3600000 毫秒），可根据需求调整
    private val CACHE_EXPIRY_TIME = 3600000L

    override fun getPagingVideos(): PagingSource<Int, VideoEntity> {
        return localDataSource.getPagingVideos()
    }

    override suspend fun syncLocalVideos(context: Context) =
        localDataSource.syncMediaStoreToRoom(context)

    override fun observeVideoChanges(): Flow<Unit> =
        localDataSource.observeVideoChanges()

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            localDataSource.clearVideoCache()
            // 清空缓存后，更新状态为“初始”
            _scanState.value = ScanState.Idle
        }
    }

    /**
     * 检查缓存是否过期：获取最新缓存的时间，与当前时间对比
     */
    private suspend fun isCacheExpired(): Boolean = withContext(Dispatchers.IO) {
        val cachedVideos = localDataSource.getCachedVideos().first()
        if (cachedVideos.isEmpty()) {
            // 缓存为空，视为过期
            return@withContext true
        }
        // 获取最新缓存的时间（最后扫描时间）
        val latestCacheTime = localDataSource.getLatestCacheTime()
        // 对比当前时间与最新缓存时间，判断是否过期
        System.currentTimeMillis() - latestCacheTime > CACHE_EXPIRY_TIME
    }

//    override fun getVideos(forceRefresh: Boolean): Flow<List<Video>> = flow {
//        try {
//            // 1. 检查是否需要强制刷新或缓存过期
//            val needScan = forceRefresh || isCacheExpired()
//            if (needScan) {
//                // 2. 开始扫描，更新状态为“扫描中”
//                _scanState.value = ScanState.Scanning
//                // 3. 调用 LocalDataSource 扫描本地视频
//                val scannedVideos = localDataSource.scanLocalVideos()
//                // 4. 扫描成功，保存到缓存
//                localDataSource.saveVideosToCache(scannedVideos)
//                // 5. 更新状态为“扫描成功”
//                _scanState.value = ScanState.Success
//                // 6. 发射扫描结果
//                emit(scannedVideos)
//            } else {
//                // 7. 缓存有效，直接从缓存获取视频
//                val cachedVideos = localDataSource.getCachedVideos().first()
//                emit(cachedVideos)
//                // 更新状态为“成功”（避免 UI 显示旧状态）
//                _scanState.value = ScanState.Success
//            }
//        } catch (e: Exception) {
//            // 8. 捕获异常，更新状态为“错误”
//            _scanState.value = ScanState.Error(e.message ?: "扫描视频失败，请检查存储权限")
//            // 发射空列表，避免上层流中断
//            emit(emptyList())
//        }
//    }
}