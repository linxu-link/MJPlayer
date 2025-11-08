package com.wujia.toolkit.system.receiver

import android.Manifest
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellSignalStrengthLte
import android.telephony.CellSignalStrengthNr
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.NETWORK_TYPE_LTE
import android.telephony.TelephonyManager.NETWORK_TYPE_NR
import androidx.core.app.ActivityCompat
import com.wujia.toolkit.HiAppGlobal

object HiConnectivityReceiver : PhoneStateListener() {

    private val listeners = mutableListOf<OnSignalChangedListener>()

    private val telephonyManager =
        HiAppGlobal.getApplication().getSystemService(TELEPHONY_SERVICE) as TelephonyManager

    // 信号强度变化回调
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkType = if (ActivityCompat.checkSelfPermission(
                    HiAppGlobal.getApplication(),
                    Manifest.permission.READ_PHONE_STATE,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 权限未授予，无法获取网络类型
                return
            } else {
                val networkType = telephonyManager.dataNetworkType
                when (networkType) {
                    NETWORK_TYPE_NR -> parse5GSignal(signalStrength)
                    NETWORK_TYPE_LTE -> parse4GSignal(signalStrength)
                    else -> {
                        // 其他网络类型（如3G、2G等）
                    }
                }
            }
        } else {
            // 处理Android Q以下版本的信号强度变化
            val networkType = telephonyManager.networkType
            when (networkType) {
                NETWORK_TYPE_LTE -> parse4GSignal(signalStrength)
                NETWORK_TYPE_NR -> parse5GSignal(signalStrength)
                else -> {
                    // 其他网络类型（如3G、2G等）
                }
            }
        }
    }

    // 4G信号解析
    private fun parse4GSignal(strength: SignalStrength) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val lteStrength =
                strength.cellSignalStrengths.filterIsInstance<CellSignalStrengthLte>().firstOrNull()
            val rsrp = lteStrength?.rsrp ?: CellInfo.UNAVAILABLE
            val rsrq = lteStrength?.rsrq ?: CellInfo.UNAVAILABLE
            // 4G信号参数范围：-44 ~ -140 dBm
            notifySignalChanged(getSignalLevel(rsrp))
        } else {
        }
    }

    // 5G信号解析（需API 29+）
    private fun parse5GSignal(strength: SignalStrength) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val nrStrength =
                strength.cellSignalStrengths.filterIsInstance<CellSignalStrengthNr>().firstOrNull()
//            val rsrp = nrStrength?.rsrp ?: CellInfo.UNAVAILABLE
//            val csiRsrp = nrStrength?.csiRsrp ?: CellInfo.UNAVAILABLE
            // 5G信号参数范围：-44 ~ -140 dBm
        }
    }

    // 释放资源
    fun stopMonitoring() {
        telephonyManager.listen(this, LISTEN_NONE)
    }

    // 注册信号监听
    fun startMonitoring() {
        telephonyManager.listen(this, LISTEN_SIGNAL_STRENGTHS or LISTEN_CELL_INFO)
    }

    fun register(listener: OnSignalChangedListener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
            if (listeners.size == 1) {
                // 注册广播接收器
                startMonitoring()
            } else if (listeners.size > 1) {
            } else {
                // do nothing
            }
        }
    }

    fun unregister(listener: OnSignalChangedListener) {
        synchronized(listeners) {
            listeners.remove(listener)
            if (listeners.isEmpty()) {
                stopMonitoring()
            }
        }
    }

    private fun getSignalLevel(rsrp: Int): Int {
        return when {
            rsrp >= -95 -> 4 // 极强
            rsrp >= -105 -> 3 // 强
            rsrp >= -115 -> 2 // 一般
            else -> 1 // 弱
        }
    }

    private fun notifySignalChanged(signalLevel: Int) {
        synchronized(listeners) {
            listeners.forEach {
                it.onSignalChanged(signalLevel)
            }
        }
    }

    interface OnSignalChangedListener {
        fun onSignalChanged(signalLevel: Int)
    }
}
