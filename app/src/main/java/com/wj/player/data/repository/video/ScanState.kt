package com.wj.player.data.repository.video

/**
 * 扫描状态：上层 UI 可根据状态显示加载中/空视图/错误提示
 */
sealed class ScanState {
    object Idle : ScanState() // 初始状态，未扫描
    object Scanning : ScanState() // 扫描中
    object Success : ScanState() // 扫描成功
    data class Error(val message: String) : ScanState() // 扫描失败（带错误信息）
}
