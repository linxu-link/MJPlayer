package com.wj.player.data.repository.video

import android.content.Context
import androidx.paging.PagingSource
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.data.entity.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 视频仓库接口：定义上层可调用的方法契约
 */
interface VideoRepository {

    // 获取Paging3视频列表
    fun getPagingVideos(): PagingSource<Int, VideoEntity>

    // 获取所有视频列表（用于播放列表等场景）
    fun getVideos(forceRefresh: Boolean): Flow<List<Video>>

    // 同步本地视频到数据库
    suspend fun syncLocalVideos(context: Context)

    // 监听视频变化
    fun observeVideoChanges(): Flow<Unit>

    /**
     * 获取扫描状态（如“扫描中”“扫描完成”“扫描失败”）
     * @return 扫描状态 StateFlow（上层可观察状态变化）
     */
    fun getScanState(): StateFlow<ScanState>

    /**
     * 清空视频缓存
     */
    suspend fun clearCache()
}
