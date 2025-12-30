package com.wj.player.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.wj.player.arch.ArchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ArchViewModel() {

    // 用StateFlow（Compose推荐）保存视频Uri，支持协程且可被Compose观察
    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    // 设置视频Uri的方法
    fun setVideoUri(uri: Uri?) {
        _videoUri.value = uri
    }

    // 清空视频Uri（播放完成/返回后重置）
    fun clearVideoUri() {
        _videoUri.value = null
    }

}
