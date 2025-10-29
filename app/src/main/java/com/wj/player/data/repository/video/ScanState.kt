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

package com.wj.player.data.repository.video

/**
 * 扫描状态：上层 UI 可根据状态显示加载中/空视图/错误提示
 */
sealed class ScanState {
    object Idle : ScanState() // 初始状态，未扫描
    object Scanning : ScanState() // 扫描中
    object Success : ScanState() // 扫描成功
    data class Error(val message: String) : ScanState() // 扫描失败（带错误信息）
}