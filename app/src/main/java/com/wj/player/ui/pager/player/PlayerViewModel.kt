package com.wj.player.ui.pager.player

import androidx.lifecycle.viewModelScope
import com.wj.player.R
import com.wj.player.arch.ArchViewModel
import com.wj.player.data.entity.Video
import com.wj.player.domian.SearchVideoByIdUseCase
import com.wj.player.utils.WhileUiSubscribed
import com.wujia.toolkit.utils.HiLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val video: Video? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null, // 用户提示消息（资源ID）
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val searchVideoById: SearchVideoByIdUseCase, // 返回值为Video
) : ArchViewModel() {

    private val _video = MutableStateFlow<Video?>(null)
    private val _userMessage = MutableStateFlow<Int?>(null)
    private val _isLoading = MutableStateFlow(false)

    // 对外暴露的UI状态（聚合所有状态流）
    val uiState: StateFlow<PlayerUiState> = combine(
        _video,
        _userMessage,
        _isLoading,
    ) { video: Video?, userMessage: Int?, isLoading: Boolean ->
        PlayerUiState(
            video = video,
            userMessage = userMessage,
            isLoading = isLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = PlayerUiState(),
    )

    fun playVideo(videoId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            val video = searchVideoById(videoId)
            if (video == null) {
                _userMessage.value = R.string.error_video_not_found
                _video.value = null
            } else {
                _video.value = video
            }
            _isLoading.value = false
            HiLog.e("videoId: $videoId, video: $video")
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

}
