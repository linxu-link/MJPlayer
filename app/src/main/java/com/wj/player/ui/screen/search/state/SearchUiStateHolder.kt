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

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class SearchUiStateHolder(
    initialSearchText: String,
    private val coroutineScope: CoroutineScope
) {
    // 搜索框输入状态（界面元素状态，随界面生命周期）
    var searchText by mutableStateOf(initialSearchText)
        private set

    // 列表滚动状态（Compose 专属状态）
    val listState = LazyListState()

    // 处理搜索框输入变化（界面逻辑）
    fun updateSearchText(text: String) {
        searchText = text
    }

    // 处理清空输入（界面逻辑）
    fun clearSearchText() {
        searchText = ""
    }

    // 处理点击历史关键词（界面逻辑：填充输入框并触发搜索）
    fun onHistoryKeywordClicked(keyword: String, onSearch: (String) -> Unit) {
        searchText = keyword
        coroutineScope.launch {
            onSearch(keyword) // 调用 ViewModel 搜索
        }
    }
}

// 在 Compose 中创建，绑定界面生命周期
@Composable
fun rememberSearchUiStateHolder(
    initialSearchText: String = "",
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): SearchUiStateHolder {
    return remember(initialSearchText, coroutineScope) {
        SearchUiStateHolder(initialSearchText, coroutineScope)
    }
}