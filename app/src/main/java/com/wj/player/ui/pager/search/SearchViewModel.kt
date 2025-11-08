package com.wj.player.ui.pager.search

import androidx.lifecycle.viewModelScope
import com.wj.player.arch.ArchViewModel
import com.wj.player.domian.ClearSearchHistoryUseCase
import com.wj.player.domian.DeleteSearchHistoryUseCase
import com.wj.player.domian.GetSearchHistoriesUseCase
import com.wj.player.domian.SaveSearchHistoryUseCase
import com.wj.player.domian.SearchVideoByKeywordUseCase
import com.wj.player.data.entity.SearchHistory
import com.wj.player.data.entity.Video
import com.wj.player.ui.pager.videolist.VideoListUiState
import com.wj.player.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoSearchUiState(
    val isLoading: Boolean = false,
    val keyword: String = "",
    val searchResults: List<Video> = emptyList(),
    val searchHistories: List<SearchHistory> = emptyList(),
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchVideosByKeyword: SearchVideoByKeywordUseCase,
    private val saveHistory: SaveSearchHistoryUseCase,
    private val getHistories: GetSearchHistoriesUseCase,
    private val deleteHistory: DeleteSearchHistoryUseCase,
    private val clearHistory: ClearSearchHistoryUseCase,
) : ArchViewModel() {

    // 加载状态（私有可变流）
    private val _isLoading = MutableStateFlow(false)

    // 搜索关键词（私有可变流）
    private val _keyword = MutableStateFlow("")

    // 搜索历史（私有可变流）
    private val _searchHistories = MutableStateFlow(emptyList<SearchHistory>())

    // 搜索结果（私有可变流）
    private val _searchResults = MutableStateFlow(emptyList<Video>())

    // 对外暴露的UI状态（聚合所有状态流）
    val uiState: StateFlow<VideoSearchUiState> = combine(
        _isLoading,
        _keyword,
        _searchResults,
        _searchHistories,
    ) { isLoading, keyword, searchResults, searchHistories ->
        VideoSearchUiState(
            isLoading = isLoading,
            keyword = keyword,
            searchResults = searchResults,
            searchHistories = searchHistories,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = VideoSearchUiState(isLoading = false),
    )


    init {
        // 初始化：获取搜索历史
        viewModelScope.launch {
            getHistories().collect { histories ->
                _searchHistories.value = histories
            }
        }
    }

    // 处理搜索关键词变化：聚合加载/结果状态
    fun onSearchKeywordChanged(keyword: String) {
        viewModelScope.launch {
            val trimmedKeyword = keyword.trim()
            // 1. 先更新为加载状态（如果关键词非空）
            if (trimmedKeyword.isNotEmpty()) {
                _isLoading.value = true
                _keyword.value = trimmedKeyword
                // 2. 执行搜索逻辑
                val results = searchVideosByKeyword(trimmedKeyword)
                // 3. 保存历史（如果有结果）
                if (trimmedKeyword.isNotEmpty()) {
                    saveHistory(trimmedKeyword, results.isNotEmpty())
                }
                // 4. 更新结果状态
                _searchResults.value = results
                // 延迟更新加载状态（避免闪烁）
                delay(600)
                _isLoading.value = false
            } else {
                _searchResults.value = emptyList()
            }

        }
    }

    // 处理删除历史（更新状态中的历史列表）
    fun onDeleteHistory(keyword: String) {
        viewModelScope.launch {
            deleteHistory(keyword)
            _searchHistories.value = getHistories().first()
        }
    }

    // 处理清空历史（同理更新状态）
    fun onClearAllHistory() {
        viewModelScope.launch {
            clearHistory()
            _searchHistories.value = emptyList()
        }
    }
}
