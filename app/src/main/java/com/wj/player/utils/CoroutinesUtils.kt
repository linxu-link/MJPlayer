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
