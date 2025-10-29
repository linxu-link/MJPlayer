package com.wj.player.ui.view.player.base

// 抽象播放器基类
abstract class BasePlayerController : IVideoPlayerController {
    protected abstract fun initializePlayer()
    protected abstract fun updateState(update: PlayerState.() -> PlayerState)

    // 公共状态管理逻辑
    protected fun handlePlaybackStateChange(isPlaying: Boolean, state: PlaybackState) {
        updateState { copy(isPlaying = isPlaying, playbackState = state) }
    }

    protected fun handlePositionUpdate(position: Long, duration: Long) {
        updateState { copy(currentPosition = position, duration = duration) }
    }

    protected fun handleBufferingUpdate(percentage: Int) {
        updateState { copy(bufferedPercentage = percentage) }
    }

    protected fun handlePlaybackSpeedUpdate(speed: Float) {
        updateState { copy(playbackSpeed = speed) }
    }

    protected fun handleError(error: Throwable) {
        updateState { copy(playbackState = PlaybackState.ERROR) }
    }
}
