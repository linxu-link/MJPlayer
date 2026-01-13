package com.wj.media

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

class HiAudioFocusHelper(private val audioManager: AudioManager) {

    private var audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                // 永久失去焦点（如其他应用开始播放音乐）
                // 应停止播放并释放资源
                pauseOrStopPlayback()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // 暂时失去焦点（如来电）
                // 暂停播放，但保留资源
                pausePlayback()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // 可以降低音量继续播放（如导航提示音）
                lowerVolume()
            }

            AudioManager.AUDIOFOCUS_GAIN -> {
                // 重新获得焦点
                // 恢复正常播放
                resumePlayback()
            }
        }
    }

    private var audioFocusRequest: AudioFocusRequest? = null

    /**
     * 请求音频焦点
     * @return 是否成功请求到音频焦点
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestAudioFocus(): Boolean {
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA) // 用途：媒体播放
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) // 类型：音乐
                    .build(),
            )
            .setWillPauseWhenDucked(true) // 表示你愿意在 duck 时暂停（可选）
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
        audioFocusRequest = request
        val result = audioManager.requestAudioFocus(request)
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    /**
     * 放弃音频焦点
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun abandonAudioFocus() {
        audioFocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
    }

    // 以下是你需要根据业务实现的方法
    private fun pauseOrStopPlayback() {
        // 停止播放，释放 MediaPlayer 等
    }

    private fun pausePlayback() {
        // 暂停播放
    }

    private fun lowerVolume() {
        // 降低音量（或暂停，取决于需求）
    }

    private fun resumePlayback() {
        // 恢复播放（如果之前暂停了）
    }

}
