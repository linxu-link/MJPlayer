package com.wj.player.ui.screen.search.state

import com.wj.player.entity.SearchHistory
import com.wj.player.entity.Video

// 密封类区分状态场景（加载/内容），确保状态完整性
sealed class VideoSearchUiState {
    abstract val keyword: String // 所有状态必有的关键词

    // loading 状态
    data class Loading(
        override val keyword: String,
    ) : VideoSearchUiState()

    // content 状态
    data class Content(
        override val keyword: String,
        val searchResults: List<Video>,
        val searchHistories: List<SearchHistory>,
    ) : VideoSearchUiState()
}
