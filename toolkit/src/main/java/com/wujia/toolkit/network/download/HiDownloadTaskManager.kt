package com.wujia.toolkit.network.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.network.download.EnhancedDownloadManager.DownloadStatusListener
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HiDownloadTaskManager"

/**
 * adb shell dumpsys download_manager
 */
object HiDownloadTaskManager {

    private val downloadTaskScope = CoroutineScope(Dispatchers.IO)
    private val statusListeners = mutableListOf<DownloadStatusListener>()
    private val application = HiAppGlobal.getApplication()
    private val systemDownloadManager by lazy {
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private val downloadManager by lazy { EnhancedDownloadManager(systemDownloadManager) }

    private val statusListener = object : DownloadStatusListener {
        override fun onStatusUpdate(
            downloadId: Long,
            status: Int,
            bytesDownloaded: Int,
            bytesTotal: Int,
        ) {
            HiLog.e(
                TAG,
                "[onStatusUpdate] status: $status, Downloaded: $bytesDownloaded, Total: $bytesTotal",
            )
            synchronized(this) {
                HiLog.d(TAG, "[onStatusUpdate] statusListener size: ${statusListeners.size}")
                statusListeners.forEach {
                    it.onStatusUpdate(downloadId, status, bytesDownloaded, bytesTotal)
                }
            }
        }
    }

    /**
     * 执行下载任务
     *
     */
    fun startDownload(url: String, fileName: String, ignoreStatus: Boolean = false) {
        HiLog.e(TAG, "[startDownload] : $url $fileName")
        downloadTaskScope.launch {
            val status = queryDownloadStatus(url)
            HiLog.e(TAG, "[startDownload] status: $status")
            if (status == HiDownloadStatus.STATUS_NONE || status == HiDownloadStatus.STATUS_FAILED || ignoreStatus) {
                val downloadId = downloadManager.startDownload(url, fileName)
                HiLog.e(TAG, "[startDownload] start! downloadId: $downloadId")
                return@launch
            } else {
                HiLog.e(TAG, "[startDownload] Download already in progress or completed: $status")
            }
        }
    }

    // 取消下载任务
    @SuppressLint("Range")
    fun cancelDownloadTask(url: String) {
        val query = DownloadManager.Query()
        val cursor = systemDownloadManager.query(query)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                HiLog.e(
                    TAG,
                    "[cancelDownloadTask] success: ${status == DownloadManager.STATUS_SUCCESSFUL}",
                )
                if (status != DownloadManager.STATUS_SUCCESSFUL) {
                    val downloadUrl =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
                    val downloadId =
                        cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                    if (url == downloadUrl) {
                        systemDownloadManager.remove(downloadId)
                        break
                    }
                    HiLog.e(TAG, "[cancelDownloadTask]: $downloadId|$url")
                }
            } while (cursor.moveToNext())
        }
    }

    // 查询所有进行中的下载任务
    @SuppressLint("Range")
    fun cancelAllDownloadTask() {
        downloadTaskScope.launch {
            HiLog.d(TAG, "[cancelAllDownloadTask]")
            val query = DownloadManager.Query()
            val cursor = systemDownloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    HiLog.e(
                        TAG,
                        "[cancelAllDownloadTask] success: ${status == DownloadManager.STATUS_SUCCESSFUL}",
                    )
                    if (status != DownloadManager.STATUS_SUCCESSFUL) {
                        val downloadUrl =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
                        val downloadId =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
                        systemDownloadManager.remove(downloadId)
                        HiLog.e(TAG, "[cancelAllDownloadTask] remove: $downloadId|$downloadUrl")
                    }
                } while (cursor.moveToNext())
            }
        }
    }

    @HiDownloadStatus
    @SuppressLint("Range")
    suspend fun queryDownloadStatus(url: String): Int {
        return withContext(Dispatchers.IO) {
            val query = DownloadManager.Query()
            val cursor = systemDownloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val downloadUrl =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
                    HiLog.d(TAG, "[queryDownloadStatus] downloadUrl: $downloadUrl")
                    if (url == downloadUrl) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        HiLog.d(TAG, "[queryDownloadStatus] status: $status")
                        when (status) {
                            DownloadManager.STATUS_PENDING -> HiDownloadStatus.STATUS_PENDING
                            DownloadManager.STATUS_RUNNING -> HiDownloadStatus.STATUS_RUNNING
                            DownloadManager.STATUS_PAUSED -> HiDownloadStatus.STATUS_PAUSED
                            DownloadManager.STATUS_SUCCESSFUL -> HiDownloadStatus.STATUS_SUCCESS
                            DownloadManager.STATUS_FAILED -> {
                                val reason =
                                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                                HiLog.d(TAG, "[queryDownloadStatus] STATUS_FAILED $reason")
                                HiDownloadStatus.STATUS_FAILED
                            }

                            else -> HiDownloadStatus.STATUS_NONE
                        }
                    }
                } while (cursor.moveToNext())
            }
            return@withContext HiDownloadStatus.STATUS_NONE
        }
    }

    // 设置状态监听器
    fun registerStatusListener(listener: DownloadStatusListener) {
        synchronized(this) {
            if (statusListeners.contains(listener).not()) {
                statusListeners.add(listener)
            }
            // 首次添加监听器时注册
            if (statusListeners.size == 1) {
                downloadManager.registerStatusListener(statusListener)
            }
        }
    }

    fun unregisterStatusListener(listener: DownloadStatusListener) {
        synchronized(this) {
            statusListeners.remove(listener)
            if (statusListeners.isEmpty()) {
                downloadManager.unregisterStatusListener()
            }
        }
    }
}
