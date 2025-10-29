package com.wj.player.backup

import com.wj.player.entity.Video

/**
 * 分页查询结果：包含当前页视频列表 + 是否有更多数据
 */
data class VideoPagingResult(
    val videos: List<Video>,
    val hasMore: Boolean,
)
