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

package com.wujia.toolkit.network.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.utils.HiLog

private const val TAG = "EnhancedDownloadManager"

class EnhancedDownloadManager(private val systemDownloadManager: DownloadManager) {
    private val context = HiAppGlobal.getApplication()
    private val mainHandler = Handler(Looper.getMainLooper())
    private var statusListeners: DownloadStatusListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val receivedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            HiLog.e(TAG, "[onReceive] downloadId : ${receivedId}")
            queryDownloadStatus(receivedId)
        }
    }

    init {
        registerReceiver()
    }

    // 开始下载（支持断点续传）
    fun startDownload(url: String, fileName: String): Long {
        val request = DownloadManager.Request(url.toUri())
        // 基础配置
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        // 关键：启用断点续传
        request.setAllowedOverRoaming(true)
        request.setAllowedOverMetered(true)
        request.allowScanningByMediaScanner()
        // 加入队列并返回任务ID
        val downloadId = systemDownloadManager.enqueue(request)
        // 注册广播接收器监听下载完成/失败
        registerReceiver()
        return downloadId
    }

    // 查询下载状态
    @SuppressLint("Range")
    fun queryDownloadStatus(downloadId: Long) {
        val query = DownloadManager.Query().apply {
            setFilterById(downloadId)
        }
        systemDownloadManager.query(query).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                // 回调状态
                notifyStatus(downloadId, status, bytesDownloaded, bytesTotal)
            }
        }
    }

    // 设置状态监听器
    fun registerStatusListener(listener: DownloadStatusListener) {
        statusListeners = listener
    }

    fun unregisterStatusListener() {
        statusListeners = null
    }

    // 注册广播接收器
    private fun registerReceiver() {
        // 注册完成和失败广播
        val filter = IntentFilter()
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        filter.addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    // 主线程回调状态
    private fun notifyStatus(downloadId: Long, status: Int, bytesDownloaded: Int, bytesTotal: Int) {
        mainHandler.post(Runnable {
            statusListeners?.onStatusUpdate(downloadId, status, bytesDownloaded, bytesTotal)
        })
    }

    // 状态监听接口
    interface DownloadStatusListener {
        fun onStatusUpdate(downloadId: Long, status: Int, bytesDownloaded: Int, bytesTotal: Int)
    }
}