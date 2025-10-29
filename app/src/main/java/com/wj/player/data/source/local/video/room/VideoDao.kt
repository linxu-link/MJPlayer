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

package com.wj.player.data.source.local.video.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    // 分页查询（Paging3必须返回PagingSource）
    @Query("SELECT * FROM videos ORDER BY updateTime DESC")
    fun getPagingVideos(): PagingSource<Int, VideoEntity>

    // 查询所有视频
    @Query("SELECT * FROM videos")
    fun getVideos(): Flow<List<VideoEntity>>

    // 批量插入
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 冲突时替换（更新视频）
    suspend fun insertVideos(videos: List<VideoEntity>)

    // 删除所有视频
    @Query("DELETE FROM videos")
    suspend fun deleteAll()

    // 监听视频总数变化（用于检测数据更新）
    @Query("SELECT COUNT(*) FROM videos")
    fun observeVideoCount(): Flow<Int>

    // 获取最新缓存的时间（用于判断缓存是否过期）
    @Query("SELECT MAX(lastScanTime) FROM videos")
    suspend fun getLatestCacheTime(): Long

    // 清空所有缓存视频
    @Query("DELETE FROM videos")
    suspend fun clearVideos()
}