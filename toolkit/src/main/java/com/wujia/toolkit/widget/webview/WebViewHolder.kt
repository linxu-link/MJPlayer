package com.wujia.toolkit.widget.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.wujia.toolkit.HiAppGlobal
import java.util.LinkedList
import java.util.concurrent.CompletableFuture

private const val TAG = "WebViewHolder"

object WebViewHolder {

    private const val INIT_POOL_SIZE = 1
    private const val MAX_POOL_SIZE = 5
    private const val IDLE_TIMEOUT = 5 * 60 * 1000L // 5分钟空闲时间

    private val availableQueue = LinkedList<WebViewWrapper>()
    private val inUseSet = mutableSetOf<WebViewWrapper>()
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        warmUpPool()
        startJanitorTask()
    }

    fun acquire(context: Context): WebView {
        return mainHandler.runOnMainSync {
            findAvailableWebView(context)?.apply {
                inUseSet.add(this)
                availableQueue.remove(this)
            }?.webView ?: createNewWebView(context).webView
        } ?: createEmergencyWebView(context)
    }

    fun release(webView: WebView) {
        mainHandler.post {
            inUseSet.find { it.webView == webView }?.let { wrapper ->
                resetWebView(wrapper.webView)
                inUseSet.remove(wrapper)
                if (availableQueue.size < MAX_POOL_SIZE) {
                    availableQueue.add(wrapper.apply { lastUsedTime = System.currentTimeMillis() })
                } else {
                    destroyWebView(wrapper.webView)
                }
            }
        }
    }


    // 立即销毁过期的webview
    fun clear() {
        cleanExpiredWebViews()
    }

    private fun warmUpPool() {
        mainHandler.post {
            repeat(INIT_POOL_SIZE) {
                availableQueue.add(createNewWebView(HiAppGlobal.getApplication()))
            }
        }
    }

    private fun findAvailableWebView(context: Context): WebViewWrapper? {
        val iterator = availableQueue.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.isValid(context)) {
                iterator.remove()
                return wrapper
            } else {
                destroyWebView(wrapper.webView)
                iterator.remove()
            }
        }
        return null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createNewWebView(context: Context): WebViewWrapper {
        return WebViewWrapper(context).apply {
            webView.apply {
                // 启动硬件加速
                setLayerType(View.LAYER_TYPE_HARDWARE, null)
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowContentAccess = true
                    allowFileAccess = true
                    builtInZoomControls = false
                    displayZoomControls = false
                    allowFileAccessFromFileURLs = true
                    allowUniversalAccessFromFileURLs = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                }
            }
        }
    }

    private fun resetWebView(webView: WebView) {
        webView.apply {
            stopLoading()
            clearHistory()
            loadUrl("about:blank")
        }
    }

    private fun destroyWebView(webView: WebView) {
        webView.apply {
            removeAllViews()
            destroy()
        }
    }

    private fun startJanitorTask() {
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                cleanExpiredWebViews()
                mainHandler.postDelayed(this, IDLE_TIMEOUT)
            }
        }, IDLE_TIMEOUT)
    }

    private fun cleanExpiredWebViews() {
        val iterator = availableQueue.iterator()
        val now = System.currentTimeMillis()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (now - wrapper.lastUsedTime > IDLE_TIMEOUT) {
                destroyWebView(wrapper.webView)
                iterator.remove()
            }
        }
    }

    private fun createEmergencyWebView(context: Context): WebView {
        return WebView(context).apply {
            settings.javaScriptEnabled = true
        }
    }


    private class WebViewWrapper(context: Context) {
        val webView = WebView(context)
        var lastUsedTime = System.currentTimeMillis()
        fun isValid(context: Context): Boolean {
            return try {
                // 检查WebView内部状态
                webView.url != null && context == webView.context
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun <T> Handler.runOnMainSync(block: () -> T): T {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return block()
        }
        val result = CompletableFuture<T>()
        post {
            try {
                result.complete(block())
            } catch (e: Exception) {
                result.completeExceptionally(e)
            }
        }
        return result.get()
    }

}