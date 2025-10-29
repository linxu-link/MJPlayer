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