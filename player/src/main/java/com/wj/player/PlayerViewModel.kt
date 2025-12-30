package com.wj.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
) : ViewModel() {

    private val repository: MediaRepository = MediaRepository(LocalMediaDataSource())
    private val _playerState = MutableStateFlow(PlayerState.Idle)
    val playerState: MutableStateFlow<PlayerState> = _playerState
    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItems: StateFlow<List<MediaItem>> = _mediaItems

    init {
        viewModelScope.launch {
            repository.getMediaItems().collect { items ->
                Log.e("PlayerViewModel", "getMediaItems: $items")
                _mediaItems.value = items
            }
        }
    }

    fun play(mediaItem: MediaItem) {
        _playerState.value = PlayerState.Playing
    }

    fun pause() {
        _playerState.value = PlayerState.Paused
    }

    fun seekTo(positionMs: Long) {
        // 由MediaSessionService处理
    }

    fun isPlaying(): Boolean {
        return _playerState.value == PlayerState.Playing
    }
}
