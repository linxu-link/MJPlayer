package com.wj.player.ui.screen.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.wj.player.arch.ArchViewModel
import com.wj.player.data.repository.search.SearchHistoryRepository
import com.wj.player.domian.SaveSearchHistoryUseCase
import com.wj.player.domian.SearchVideoByKeywordUseCase
import com.wj.player.entity.Video
import com.wj.player.ui.screen.search.state.VideoSearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchVideos: SearchVideoByKeywordUseCase,
    private val saveHistory: SaveSearchHistoryUseCase,
    private val historyRepository: SearchHistoryRepository,
) : ArchViewModel() {

    // 内部维护聚合状态（可变）
    private val _uiState = MutableStateFlow<VideoSearchUiState>(
        VideoSearchUiState.Content(
            keyword = "",
            searchResults = emptyList(),
            searchHistories = emptyList(),
        ),
    )

    // 对外暴露不可变状态流（唯一可信来源）
    val uiState: StateFlow<VideoSearchUiState> = _uiState.asStateFlow()

    // 初始化：监听搜索历史变化，更新状态
    init {
        viewModelScope.launch {
            historyRepository.getSearchHistories().collect { histories ->
                val current = _uiState.value
                // 仅当当前是内容状态且关键词为空（显示历史）时，更新历史数据
                if (current is VideoSearchUiState.Content && current.keyword.isEmpty()) {
                    _uiState.value = current.copy(searchHistories = histories)
                }
            }
        }
    }

    // 处理搜索关键词变化：聚合加载/结果状态
    fun onSearchKeywordChanged(keyword: String) {
        viewModelScope.launch {
            val trimmedKeyword = keyword.trim()
            // 1. 先更新为加载状态（如果关键词非空）
            if (trimmedKeyword.isNotEmpty()) {
                _uiState.value = VideoSearchUiState.Loading(trimmedKeyword)
            }

            // 2. 执行搜索逻辑
//            val results = searchVideos(trimmedKeyword)
            val results = listOf<Video>()
            // 3. 保存历史（如果有结果）
            if (trimmedKeyword.isNotEmpty()) {
                saveHistory(trimmedKeyword, results.isNotEmpty())
            }

            // 4. 更新为内容状态（统一聚合结果/历史）
            val currentHistories = if (trimmedKeyword.isEmpty()) {
                historyRepository.getSearchHistories().first() // 关键词为空时实时获取历史
            } else {
                emptyList() // 关键词非空时无需历史数据
            }
            _uiState.value = VideoSearchUiState.Content(
                keyword = trimmedKeyword,
                searchResults = results,
                searchHistories = currentHistories,
            )
        }
    }

    // 处理删除历史（更新状态中的历史列表）
    fun onDeleteHistory(keyword: String) {
        viewModelScope.launch {
            historyRepository.deleteSearchHistory(keyword)
            // 删除后更新状态中的历史数据（仅当当前显示历史时）
            val current = _uiState.value
            if (current is VideoSearchUiState.Content && current.keyword.isEmpty()) {
                val updatedHistories = historyRepository.getSearchHistories().first()
                _uiState.value = current.copy(searchHistories = updatedHistories)
            }
        }
    }

    // 处理清空历史（同理更新状态）
    fun onClearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearAllSearchHistory()
            val current = _uiState.value
            if (current is VideoSearchUiState.Content && current.keyword.isEmpty()) {
                _uiState.value = current.copy(searchHistories = emptyList())
            }
        }
    }
}
