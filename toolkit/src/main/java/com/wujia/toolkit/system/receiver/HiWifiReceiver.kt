package com.wujia.toolkit.system.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import com.wujia.toolkit.HiAppGlobal

private const val TAG = "WifiReceiver"

object HiWifiReceiver {

    private val listeners = mutableListOf<WifiStateListener>()
    private val wifiManager by lazy {
        HiAppGlobal.getApplication().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
    private val connectivityManager by lazy {
        HiAppGlobal.getApplication().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // 动态监听变化（通过 BroadcastReceiver）
    private val wifiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                WifiManager.RSSI_CHANGED_ACTION -> {
                    // 获取 WiFi 信号强度 (单位 dBm)
                    val newRssi = wifiManager.connectionInfo?.rssi ?: 0
                    // 判断当前api等级
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val newLevel = wifiManager?.calculateSignalLevel(newRssi) ?: 0
                        Log.i(TAG, "onReceive: newRssi = $newRssi, newLevel = $newLevel")
                        notifyWifiLevelChange(newRssi, newLevel)
                    } else {
                        // 低版本的API，需要自己计算
                        val newLevel = WifiManager.calculateSignalLevel(newRssi, 5)
                        Log.i(TAG, "onReceive: newRssi = $newRssi, newLevel = $newLevel")
                        notifyWifiLevelChange(newRssi, newLevel + 1)
                    }
                }

                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN
                    )
                    val enabled = state == WifiManager.WIFI_STATE_ENABLED
                    notifyWifiEnabled(enabled)
                }

                WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                    val networkInfo = intent.getParcelableExtra<NetworkInfo>(
                        WifiManager.EXTRA_NETWORK_INFO
                    )
                    val connected = networkInfo?.isConnected == true
                    val ssid = if (connected) getCurrentSsid() else null
                    notifyWifiConnected(connected, ssid)
                }
            }
        }
    }

    private fun checkCurrentState(listener: WifiStateListener) {
        // 检查初始状态
        listener.onWifiEnabled(wifiManager.isWifiEnabled)
        // 检查当前连接状态
        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isWifiConnected = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        val ssid = if (isWifiConnected) getCurrentSsid() else null
        listener.onWifiConnected(isWifiConnected, ssid)
    }

    private fun getCurrentSsid(): String? {
        return try {
            val wifiInfo = wifiManager.connectionInfo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                wifiInfo.ssid?.removeSurrounding("\"")
            } else {
                @Suppress("DEPRECATION")
                wifiInfo.ssid?.removeSurrounding("\"")
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun notifyWifiLevelChange(rssi: Int, level: Int) {
        synchronized(listeners) {
            listeners.forEach {
                it.onSignalStrengthChanged(rssi, level)
            }
        }
    }

    private fun notifyWifiEnabled(enabled: Boolean) {
        synchronized(listeners) {
            listeners.forEach {
                it.onWifiEnabled(enabled)
            }
        }
    }

    private fun notifyWifiConnected(connected: Boolean, ssid: String?) {
        synchronized(listeners) {
            listeners.forEach {
                it.onWifiConnected(connected, ssid)
            }
        }
    }

    /**********************************************************************************************/

    fun register(listener: WifiStateListener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }

            if (listeners.size == 1) {
                // 注册多个广播接收
                val filter = IntentFilter().apply {
                    addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
                    addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                    addAction(WifiManager.RSSI_CHANGED_ACTION)
                }
                HiAppGlobal.getApplication().registerReceiver(
                    wifiReceiver,
                    filter
                )
            } else if (listeners.size > 1) {
                checkCurrentState(listener)
            } else {
                // do nothing
            }
        }
    }

    fun unregister(listener: WifiStateListener) {
        synchronized(listeners) {
            listeners.remove(listener)
            if (listeners.isEmpty()) {
                HiAppGlobal.getApplication().unregisterReceiver(wifiReceiver)
            }
        }
    }

    interface WifiStateListener {
        fun onWifiEnabled(enabled: Boolean) {}
        fun onWifiConnected(connected: Boolean, ssid: String? = null) {}
        fun onSignalStrengthChanged(rssi: Int, level: Int) {}
    }

}