package com.wujia.toolkit.utils.ktx

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.core.net.toUri
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 加载视频缩略图 - 原生方式
 */
suspend fun loadVideoThumbnailNative(
    videoId: Long,
    videoPath: String,
    thumbnailPath: String,
): Bitmap? = withContext(Dispatchers.IO) {
    HiLog.e("loadVideoThumbnailNative: videoId=$videoId, videoPath=$videoPath, thumbnailPath=$thumbnailPath")
    return@withContext try {
        // 方法1：通过视频ID从MediaStore获取
        if (videoId > 0) {
            loadThumbnailFromMediaStore(videoId)
        }
        // 方法2：优先使用我们存储的缩略图路径
        else  if (thumbnailPath.isNotEmpty()) {
            loadThumbnailFromPath(thumbnailPath)
        }
        // 方法3：最后使用视频文件路径生成缩略图
        else {
            generateThumbnailFromVideo(videoPath)
        }
    } catch (e: Exception) {
        HiLog.e("加载缩略图失败: ${e.message}")
        null
    }
}

/**
 * 从文件路径加载缩略图
 */
private fun loadThumbnailFromPath(thumbnailPath: String): Bitmap? {
    return try {
        if (thumbnailPath.startsWith("content://")) {
            // 处理 content URI
            HiAppGlobal.getApplication().contentResolver.openInputStream(thumbnailPath.toUri())
                ?.use { inputStream ->
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = false
                        inSampleSize = 2 // 缩小采样，避免内存溢出
                        inPreferredConfig = Bitmap.Config.RGB_565 // 降低内存占用
                    }
                    BitmapFactory.decodeStream(inputStream, null, options)
                }
        } else {
            // 处理文件路径
            BitmapFactory.decodeFile(thumbnailPath)
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * 从 MediaStore 获取缩略图
 */
@Suppress("DEPRECATION")
private fun loadThumbnailFromMediaStore(videoId: Long): Bitmap? {
    val context = HiAppGlobal.getApplication()
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ 使用新的API
            val uri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoId,
            )
            context.contentResolver.loadThumbnail(uri, Size(250, 250), null)
        } else {
            // 旧版本使用 ThumbnailUtils
            MediaStore.Video.Thumbnails.getThumbnail(
                context.contentResolver,
                videoId,
                MediaStore.Video.Thumbnails.MINI_KIND,
                null,
            )
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * 从视频文件生成缩略图
 */
@Suppress("DEPRECATION")
private fun generateThumbnailFromVideo(videoPath: String): Bitmap? {
    return try {
        ThumbnailUtils.createVideoThumbnail(
            videoPath,
            MediaStore.Video.Thumbnails.MINI_KIND,
        )
    } catch (e: Exception) {
        null
    }
}
