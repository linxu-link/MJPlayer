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

package com.wj.player.backup

/**
 * 分页查询参数：用于 MediaStore 分页扫描视频
 * @param page 页码（从 0 开始）
 * @param pageSize 每页数量
 */
data class VideoPagingParams(
    val page: Int = 0,
    val pageSize: Int = 20 // 每页默认20条，可调整
)