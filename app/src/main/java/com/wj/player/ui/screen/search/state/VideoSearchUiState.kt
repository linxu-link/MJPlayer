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

package com.wj.player.ui.screen.search.state

import com.wj.player.entity.SearchHistory
import com.wj.player.entity.Video

// 密封类区分状态场景（加载/内容），确保状态完整性
sealed class VideoSearchUiState {
    abstract val keyword: String // 所有状态必有的关键词

    // loading 状态
    data class Loading(
        override val keyword: String
    ) : VideoSearchUiState()

    // content 状态
    data class Content(
        override val keyword: String,
        val searchResults: List<Video>,
        val searchHistories: List<SearchHistory>
    ) : VideoSearchUiState()
}