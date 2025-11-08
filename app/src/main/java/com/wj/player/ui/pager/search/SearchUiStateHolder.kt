package com.wj.player.ui.pager.search

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

// 隔离搜索界面的状态管理与交互逻辑
//@Stable 是 Compose 中的稳定性注解，告诉 Compose 该类的实例及其状态变化是可预测的：
//当类的属性变化时，会触发依赖它的 Composable 重组。
//若属性未变化，Compose 可跳过不必要的重组，优化性能。
//对于持有 mutableStateOf 等可观察状态的类，标记 @Stable 能帮助 Compose 更高效地管理重组。
@Stable
class SearchUiStateHolder(
    initialSearchText: String,
    private val coroutineScope: CoroutineScope,
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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): SearchUiStateHolder {
    return remember(initialSearchText, coroutineScope) {
        SearchUiStateHolder(initialSearchText, coroutineScope)
    }
}
