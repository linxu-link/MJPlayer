package com.wj.player.data.source.local.video.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wj.player.entity.Video

/**
 * 视频本地实体：Room 数据库表结构，仅用于本地缓存
 */
@Entity(tableName = "videos") // Room实体注解
data class VideoEntity(
    @PrimaryKey val id: Long, // 视频唯一ID（MediaStore的_id）
    val title: String, // 视频标题
    val path: String, // 视频文件路径
    val duration: Long, // 视频时长（毫秒）
    val size: Long, // 视频文件大小（字节）
    val thumbnailPath: String? = null, // 缩略图路径（可选）
    @ColumnInfo(defaultValue = "0") val updateTime: Long, // 最后更新时间（用于监听变化）
    val lastScanTime: Long,
)

// 扩展函数：Entity 转 Domain Model（Repository 中使用）
fun VideoEntity.toDomainModel(): Video {
    return Video(
        id = id,
        path = path,
        title = title,
        duration = duration,
        size = size,
        updateTime = updateTime,
        thumbnailPath = thumbnailPath,
        lastScanTime = lastScanTime,
    )
}
