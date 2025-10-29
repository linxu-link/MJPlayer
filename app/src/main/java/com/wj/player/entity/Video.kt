package com.wj.player.entity

import com.wj.player.data.source.local.video.room.VideoEntity

/**
 * 视频领域模型：UI 和 ViewModel 直接使用
 * @param id 视频唯一标识（MediaStore 中的 ID）
 * @param path 视频文件路径
 * @param title 视频标题
 * @param duration 视频时长（毫秒）
 * @param size 视频文件大小（字节）
 * @param thumbnailPath 视频缩略图路径（可选）
 */
data class Video(
    val id: Long,
    val path: String,
    val title: String,
    val duration: Long,
    val size: Long,
    val updateTime: Long,
    val thumbnailPath: String? = null,
    val lastScanTime: Long = 0,
)

fun Video.toRoomModel(): VideoEntity = VideoEntity(
    id = id,
    path = path,
    title = title,
    duration = duration,
    size = size,
    updateTime = updateTime,
    thumbnailPath = thumbnailPath,
    lastScanTime = lastScanTime,
)
