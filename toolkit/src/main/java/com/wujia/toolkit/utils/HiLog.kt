package com.wujia.toolkit.utils

class HiLog {
    interface HiLogDelegate {
        fun e(tag: String, msg: String, vararg obj: Any)
        fun w(tag: String, msg: String, vararg obj: Any)
        fun i(tag: String, msg: String, vararg obj: Any)
        fun d(tag: String, msg: String, vararg obj: Any)
        fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any)
    }

    companion object {
        private var sDelegate: HiLogDelegate? = null
        private val LOG_CLASS_NAME = HiLog::class.java.name

        fun setDelegate(delegate: HiLogDelegate) {
            sDelegate = delegate
        }

        /**
         * 获取调用方类名作为TAG
         */
        private fun getCallerTag(): String {
            val stackTrace = Thread.currentThread().stackTrace
            // 遍历调用栈，找到第一个非HiLog的调用者
            for (element in stackTrace) {
                val className = element.className
                if (className != LOG_CLASS_NAME) {
                    // 提取简单类名（去掉包名）
                    val lastDotIndex = className.lastIndexOf('.')
                    return if (lastDotIndex != -1 && lastDotIndex < className.length - 1) {
                        className.substring(lastDotIndex + 1)
                    } else {
                        className
                    }
                }
            }
            return "Unknown"
        }

        /**
         * 获取调用方的堆栈信息（排除日志框架自身的调用）
         */
        private fun getCallerStackTrace(): Array<StackTraceElement> {
            val fullStackTrace = Thread.currentThread().stackTrace
            // 过滤掉日志框架自身的调用栈元素
            return fullStackTrace.filter { it.className != LOG_CLASS_NAME }.toTypedArray()
        }

        // 日志方法实现（自动填充TAG）
        fun e(msg: String, vararg obj: Any) {
            sDelegate?.e(getCallerTag(), msg, *obj)
        }

        fun w(msg: String, vararg obj: Any) {
            sDelegate?.w(getCallerTag(), msg, *obj)
        }

        fun i(msg: String, vararg obj: Any) {
            sDelegate?.i(getCallerTag(), msg, *obj)
        }

        fun d(msg: String, vararg obj: Any) {
            sDelegate?.d(getCallerTag(), msg, *obj)
        }

        /**
         * 打印异常堆栈，包含调用方的堆栈信息
         */
        fun printErrStackTrace(tr: Throwable, format: String, vararg obj: Any) {
            sDelegate?.let { delegate ->
                val tag = getCallerTag()
                // 将调用方堆栈信息附加到异常中
                val callerStackTrace = getCallerStackTrace()
                val newThrowable = Throwable(String.format(format, *obj), tr).apply {
                    stackTrace = callerStackTrace
                }
                delegate.printErrStackTrace(tag, newThrowable, format, *obj)
            }
        }
    }
}