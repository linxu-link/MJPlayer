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

package com.wujia.toolkit.network.download

annotation class HiDownloadStatus() {
    companion object {
        const val STATUS_NONE = 0 // 未开始
        const val STATUS_PENDING = 1 // 等待中
        const val STATUS_RUNNING = 2 // 下载中
        const val STATUS_PAUSED = 3 // 暂停
        const val STATUS_SUCCESS = 4 // 成功
        const val STATUS_FAILED = 5 // 失败
    }
}