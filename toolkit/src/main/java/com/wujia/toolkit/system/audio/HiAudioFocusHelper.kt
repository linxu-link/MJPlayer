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

package com.wujia.toolkit.system.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.annotation.IntDef
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "AudioHelper"

// 音频焦点状态定义
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    AudioManager.AUDIOFOCUS_NONE,
    AudioManager.AUDIOFOCUS_GAIN,
    AudioManager.AUDIOFOCUS_LOSS,
    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
)
annotation class AudioFocusState

class HiAudioFocusHelper(
    private val audioAttributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build(),
    var onFocusGained: (() -> Unit)? = null,
    var onFocusLost: ((isTransient: Boolean) -> Unit)? = null,
    var onShouldDuck: (() -> Unit)? = null
) {
    private val audioManager: AudioManager by lazy {
        HiAppGlobal.getApplication().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val _focusState = MutableStateFlow(AudioManager.AUDIOFOCUS_NONE)
    val focusState: StateFlow<Int> = _focusState

    private var focusRequest: AudioFocusRequest? = null

    private val focusListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        _focusState.value = focusChange
        HiLog.d(TAG, "onAudioFocusChange: $focusChange")
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // 重新获得焦点
                HiLog.d(TAG, "AUDIOFOCUS_GAIN")
                onFocusGained?.invoke()
            }

            AudioManager.AUDIOFOCUS_LOSS -> {
                // 永久失去焦点
                HiLog.d(TAG, "AUDIOFOCUS_LOSS")
                onFocusLost?.invoke(false)
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // 暂时失去焦点
                HiLog.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT")
                onFocusLost?.invoke(true)
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // 可以降低音量
                HiLog.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                onShouldDuck?.invoke()
            }
        }
    }

    /**
     * 请求音频焦点
     * @param focusGain 焦点类型，默认为 AUDIOFOCUS_GAIN
     * @param onFocusGained 获得焦点回调
     * @param onFocusLost 失去焦点回调
     * @param onShouldDuck 应该降低音量回调
     * @return 是否成功获取焦点
     */
    fun requestAudioFocus(
        @AudioFocusState focusGain: Int = AudioManager.AUDIOFOCUS_GAIN,
    ): Boolean {
        val request = AudioFocusRequest.Builder(focusGain)
            .setAudioAttributes(audioAttributes)
            .setOnAudioFocusChangeListener(focusListener)
            .build()
        focusRequest = request
        val result = audioManager.requestAudioFocus(request)
        val granted = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        if (granted){
            onFocusGained?.invoke()
        }
        HiLog.d(TAG, "[requestAudioFocus]: $result")
        return granted
    }

    /**
     * 释放音频焦点
     */
    fun releaseAudioFocus() {
        HiLog.d(TAG, "[releaseAudioFocus]")
        focusRequest?.let {
            audioManager.abandonAudioFocusRequest(it)
            focusRequest = null
        }
        _focusState.value = AudioManager.AUDIOFOCUS_NONE
    }

    /**
     * 检查当前是否有音频焦点
     */
    fun hasFocus(): Boolean {
        return _focusState.value == AudioManager.AUDIOFOCUS_GAIN
    }

}