package com.wj.player.data.source.local.video

import android.content.Context
import androidx.paging.PagingSource
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.data.entity.Video
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
