package com.wj.player.backup

/**
 * 分页查询参数：用于 MediaStore 分页扫描视频
 * @param page 页码（从 0 开始）
 * @param pageSize 每页数量
 */
data class VideoPagingParams(
    val page: Int = 0,
    val pageSize: Int = 20, // 每页默认20条，可调整
)
