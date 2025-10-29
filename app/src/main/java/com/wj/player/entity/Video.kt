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
    val lastScanTime: Long = 0
)

fun Video.toRoomModel(): VideoEntity = VideoEntity(
    id = id,
    path = path,
    title = title,
    duration = duration,
    size = size,
    updateTime = updateTime,
    thumbnailPath = thumbnailPath,
    lastScanTime = lastScanTime
)
