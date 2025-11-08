package com.wj.player.ui.pager.videolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wj.player.R
import com.wj.player.data.entity.LayoutType
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.domian.GetPagingVideosUseCase
import com.wj.player.utils.WhileUiSubscribed
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// 视频列表UI状态（仅保留核心状态字段）
data class VideoListUiState(
    val videos: Flow<PagingData<VideoEntity>> = flowOf(PagingData.empty()), // 分页视频数据
    val layoutType: LayoutType = LayoutType.List, // 当前筛选类型
    val isLoading: Boolean = false, // 加载状态标记
    val userMessage: Int? = null, // 用户提示消息（资源ID）
)

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getPagingVideosUseCase: GetPagingVideosUseCase,
    application: Application,
) : AndroidViewModel(application) {

    // 加载状态（私有可变流）
    private val _isLoading = MutableStateFlow(false)

    // 用户消息（私有可变流）
    private val _userMessage = MutableStateFlow<Int?>(null)

    // 布局类型（私有可变流）
    private val _layoutType = MutableStateFlow(LayoutType.List)

    // 分页视频数据流（直接从用例获取，无需筛选）
    private val _videosFlow = getPagingVideosUseCase().cachedIn(viewModelScope) // 分页流缓存，避免重复加载

    // 对外暴露的UI状态（聚合所有状态流）
    val uiState: StateFlow<VideoListUiState> = combine(
        _isLoading,
        _userMessage,
        _layoutType,
    ) { isLoading, userMessage, layoutType ->
        VideoListUiState(
            videos = _videosFlow, // 直接使用原始的分页数据流（Flow类型）
            layoutType = layoutType,
            isLoading = isLoading,
            userMessage = userMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = VideoListUiState(isLoading = true),
    )

    init {
        syncVideos()
    }

    // 加载视频（首次加载/手动刷新）
    fun syncVideos() {
        _isLoading.value = true // 开始加载
        viewModelScope.launch {
            try {
                // 同步MediaStore视频到Room
                getPagingVideosUseCase.syncLocalVideos(getApplication())
                _userMessage.value = R.string.videos_loaded_success // 加载成功提示
            } catch (e: Exception) {
                _userMessage.value = R.string.failed_to_load_videos // 加载失败提示
                e.printStackTrace()
            } finally {
                _isLoading.value = false // 结束加载（无论成功失败）
            }
        }
    }

    // 清除提示消息（供UI调用：如Snackbar显示后）
    fun clearUserMessage() {
        _userMessage.value = null
    }

    // 监听视频变化：数据变化时自动刷新
    private fun observeVideoChanges() {
        viewModelScope.launch {
            getPagingVideosUseCase.observeVideoChanges().collect {
                HiLog.i("视频变化事件触发")
//                syncVideos() // 视频变化时重新加载列表
            }
        }
    }

    fun setLayoutType(type: LayoutType) {
        _layoutType.value = type
    }
}
