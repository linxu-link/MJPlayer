package com.wj.player.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wj.player.ui.screen.search.component.EmptyResultHint
import com.wj.player.ui.screen.search.component.LoadingIndicator
import com.wj.player.ui.screen.search.component.SearchHistoryList
import com.wj.player.ui.screen.search.component.SearchResultList
import com.wj.player.ui.screen.search.component.SearchTextField
import com.wj.player.ui.screen.search.state.VideoSearchUiState
import com.wj.player.ui.screen.search.state.rememberSearchUiStateHolder
import com.wj.player.ui.screen.search.viewmodel.SearchViewModel

@Composable
fun SearcherScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 仅观察一个状态流（符合单一来源原则）
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateHolder = rememberSearchUiStateHolder() // 界面逻辑状态容器
    val focusRequester = remember { FocusRequester() }

    // 初始化：聚焦搜索框
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // 状态驱动搜索框输入（关键词变化时同步输入框）
    LaunchedEffect(uiState) {
        when (uiState) {
            is VideoSearchUiState.Loading -> {
                uiStateHolder.updateSearchText(uiState.keyword)
            }

            is VideoSearchUiState.Content -> {
                uiStateHolder.updateSearchText(uiState.keyword)
            }
        }
    }

    // 输入框变化时触发搜索（事件向上传递）
    val searchText = uiStateHolder.searchText
    LaunchedEffect(searchText) {
        // 避免重复触发（仅当输入与状态中的关键词不一致时）
        val currentKeyword = when (uiState) {
            is VideoSearchUiState.Loading -> uiState.keyword
            is VideoSearchUiState.Content -> uiState.keyword
        }
        if (searchText != currentKeyword) {
            viewModel.onSearchKeywordChanged(searchText)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // 搜索框（状态从界面逻辑容器获取）
        SearchTextField(
            text = searchText,
            onTextChange = uiStateHolder::updateSearchText,
            onClearClick = uiStateHolder::clearSearchText,
            onBackClick = onBackClick,
            focusRequester = focusRequester,
        )

        // 内容区：基于单一状态渲染
        when (uiState) {
            is VideoSearchUiState.Loading -> {
                LoadingIndicator() // 加载状态
            }

            is VideoSearchUiState.Content -> {
                val content = uiState as VideoSearchUiState.Content
                when {
                    // 有搜索内容：显示结果
                    content.keyword.isNotEmpty() -> {
                        if (content.searchResults.isEmpty()) {
                            EmptyResultHint(keyword = content.keyword)
                        } else {
                            SearchResultList(
                                videos = content.searchResults,
                                listState = uiStateHolder.listState,
                                keyword = content.keyword,
                            )
                        }
                    }
                    // 无搜索内容：显示历史
                    else -> {
                        SearchHistoryList(
                            histories = content.searchHistories,
                            onHistoryClick = {
                                uiStateHolder.onHistoryKeywordClicked(it.keyword) {
                                    viewModel.onSearchKeywordChanged(
                                        it,
                                    )
                                }
                            },
                            onDeleteClick = viewModel::onDeleteHistory,
                            onClearAllClick = viewModel::onClearAllHistory,
                        )
                    }
                }
            }
        }
    }
}
