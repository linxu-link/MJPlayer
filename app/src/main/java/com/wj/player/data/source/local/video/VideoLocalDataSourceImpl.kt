package com.wj.player.data.source.local.video

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import com.wj.player.data.source.local.video.room.VideoDao
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.data.source.local.video.room.toDomainModel
import com.wj.player.di.IODispatcher
import com.wj.player.data.entity.Video
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 视频本地数据源实现类：具体处理 MediaStore 扫描和 Room 缓存
 */
private const val TAG = "VideoLocalDataSourceImpl"

class VideoLocalDataSourceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val videoDao: VideoDao,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : VideoLocalDataSource {

    private   val projection = arrayOf(
        MediaStore.Video.Media._ID, // 视频ID
        MediaStore.Video.Media.TITLE, // 标题
        MediaStore.Video.Media.DATA, // 文件路径
        MediaStore.Video.Media.DURATION, // 时长
        MediaStore.Video.Media.SIZE, // 大小
        MediaStore.Video.Media.DATE_MODIFIED, // 最后修改时间
        MediaStore.Video.Media.MINI_THUMB_MAGIC, // 缩略图 Uri
    )

    init {
        // 1. 创建ContentObserver，监听MediaStore视频变化
        val videoObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                HiLog.i(TAG, "MediaStore视频变化，触发刷新")
                // 2. 变化时更新视频缓存
                CoroutineScope(ioDispatcher).launch {
                    syncMediaStoreToRoom(context)
                }
            }
        }
        // 3. 注册监听
        context.contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            videoObserver,
        )
    }

    // Paging3数据源：从Room分页读取视频
    override fun getPagingVideos(): PagingSource<Int, VideoEntity> = videoDao.getPagingVideos()

    // 同步MediaStore视频到Room（核心：读取本地视频并更新数据库）
    override suspend fun syncMediaStoreToRoom(context: Context) = withContext(ioDispatcher) {
        // 1. 从MediaStore查询视频（筛选MP4等常见格式）
        val videoUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
        // 按修改时间倒序（最新的在前）
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        val videos = mutableListOf<VideoEntity>()
        context.contentResolver.query(videoUri, projection, null, null, sortOrder)?.use { cursor ->
            while (cursor.moveToNext()) {
                val videoId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))

                // 新的缩略图获取方法
                val thumbnailPath = getVideoThumbnailPath(context, videoId)

                val video = VideoEntity(
                    id = videoId,
                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)),
                    path = path,
                    duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)),
                    size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)),
                    updateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)),
                    thumbnailPath = thumbnailPath,
                    lastScanTime = System.currentTimeMillis(),
                )
                HiLog.e("video: ${video.thumbnailPath}")
                videos.add(video)
            }
        }
        // 3. 批量更新Room数据库（先删后插，避免重复）
        videoDao.deleteAll()
        videoDao.insertVideos(videos)
    }

    /**
     * 统一的缩略图获取方法
     */
    private fun getVideoThumbnailPath(context: Context, videoId: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API 29+：使用新的缩略图获取方式
            getModernThumbnailPath(context, videoId)
        } else {
            // 低版本：使用原有的查询方式
            getLegacyThumbnailPath(context, videoId) ?: ""
        }
    }

    /**
     * 现代 Android 版本获取缩略图的方法（API 29+）
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getModernThumbnailPath(context: Context, videoId: Long): String {
        return try {
            // 方法1：使用 Thumbnails.getThumbnail 获取缩略图
            val thumbnailUri = MediaStore.Video.Thumbnails.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val thumbnailProjection = arrayOf(MediaStore.Video.Thumbnails.DATA)
            val selection = "${MediaStore.Video.Thumbnails.VIDEO_ID} = ?"
            val selectionArgs = arrayOf(videoId.toString())

            context.contentResolver.query(
                thumbnailUri,
                thumbnailProjection,
                selection,
                selectionArgs,
                null
            )?.use { thumbCursor ->
                if (thumbCursor.moveToFirst()) {
                    thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
                } else {
                    // 如果查询不到，返回内容 URI
                    ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        videoId
                    ).toString()
                }
            } ?: ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoId
            ).toString()
        } catch (e: Exception) {
            HiLog.e(TAG, "获取缩略图失败: ${e.message}")
            // 返回视频的内容 URI 作为备用
            ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId).toString()
        }
    }

    /**
     * 低版本获取视频缩略图路径（保持不变）
     */
    private fun getLegacyThumbnailPath(context: Context, videoId: Long): String? {
        val thumbUri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
        val thumbProjection = arrayOf(MediaStore.Video.Thumbnails.DATA)
        val selection = "${MediaStore.Video.Thumbnails.VIDEO_ID} = ?"
        val selectionArgs = arrayOf(videoId.toString())

        return context.contentResolver.query(
            thumbUri,
            thumbProjection,
            selection,
            selectionArgs,
            null
        )?.use { thumbCursor ->
            if (thumbCursor.moveToFirst()) {
                thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
            } else {
                null
            }
        }
    }

    // 监听视频变化：通过Room的COUNT查询，当数据总数变化时发射信号
    override fun observeVideoChanges(): Flow<Unit> =
        videoDao.observeVideoCount().map { Unit } // 只关心“变化”事件，不关心具体数量

    override fun getCachedVideos(): Flow<List<Video>> {
        // 从 DAO 获取 Entity 流，转换为 Domain Model 流（上层无需知道 Entity）
        return videoDao.getVideos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun clearVideoCache() {
        withContext(Dispatchers.IO) {
            videoDao.clearVideos()
        }
    }

    override suspend fun getLatestCacheTime(): Long {
        return videoDao.getLatestCacheTime()
    }

}
