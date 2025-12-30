package com.wj.player

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepository(private val dataSource: MediaDataSource) {
    fun getMediaItems(): Flow<List<MediaItem>> = dataSource.getMediaItems()
}

interface MediaDataSource {
    fun getMediaItems(): Flow<List<MediaItem>>
}

class LocalMediaDataSource : MediaDataSource {
    override fun getMediaItems(): Flow<List<MediaItem>> = flow {

        Log.e("LocalMediaDataSource", "getMediaItems")

        emit(listOf(
            MediaItem.Builder()
                .setMediaId("video1")
                .setUri(Uri.parse("https://www.w3school.com.cn/i/movie.mp4"))
                .build(),
            MediaItem.Builder()
                .setMediaId("video2")
                .setUri(Uri.parse("https://www.w3school.com.cn/i/movie.mp4"))
                .build()
        ))
    }
}
