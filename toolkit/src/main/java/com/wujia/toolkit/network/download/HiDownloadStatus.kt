package com.wujia.toolkit.network.download

annotation class HiDownloadStatus() {
    companion object {
        const val STATUS_NONE = 0 // 未开始
        const val STATUS_PENDING = 1 // 等待中
        const val STATUS_RUNNING = 2 // 下载中
        const val STATUS_PAUSED = 3 // 暂停
        const val STATUS_SUCCESS = 4 // 成功
        const val STATUS_FAILED = 5 // 失败
    }
}
