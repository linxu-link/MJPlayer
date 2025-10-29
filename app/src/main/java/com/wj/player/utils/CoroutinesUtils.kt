package com.wj.player.utils

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

/**
 * 这个 WhileUiSubscribed 策略主要用于 StateFlow 向 UI 层暴露数据时的配置，其核心特性是：
 * 当 UI 停止观察数据时，上游数据流会保持活跃一段时间（这里是 5 秒）
 * 这一机制是为了应对短暂的配置变更（如屏幕旋转），避免频繁停止和重启数据流
 * 如果 UI 停止观察的时间超过超时时间，上游数据流会停止以节省资源，但缓存会保留
 * 当 UI 再次开始观察时，会重放最新的值并重新启动上游数据流
 * 这种策略平衡了资源消耗和用户体验，既在应用后台时节省资源，又能让用户在快速切换应用时获得流畅体验。
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
