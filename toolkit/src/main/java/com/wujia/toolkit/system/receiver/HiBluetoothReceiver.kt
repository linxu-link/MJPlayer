package com.wujia.toolkit.system.receiver

object HiBluetoothReceiver {

    private val listeners = mutableListOf<OnBluetoothChangedListener>()

    fun register(listener: OnBluetoothChangedListener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
        }
    }

    fun unregister(listener: OnBluetoothChangedListener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    fun notifyBluetoothChanged(isOpen: Boolean) {
        synchronized(listeners) {
            listeners.forEach {
                it.onBluetoothChanged(isOpen)
            }
        }
    }

    interface OnBluetoothChangedListener {
        fun onBluetoothChanged(isOpen: Boolean)
    }
}
