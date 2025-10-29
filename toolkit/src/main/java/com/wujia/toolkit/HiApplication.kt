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

package com.wujia.toolkit

import android.app.Application
import android.content.Context
import com.wujia.toolkit.utils.HiLog

open class HiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initLog()
    }

    private fun initLog() {
        HiLog.setDelegate(object : HiLog.HiLogDelegate {
            override fun e(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.e(tag, msg.format(*obj))
            }

            override fun w(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.w(tag, msg.format(*obj))
            }

            override fun i(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.i(tag, msg.format(*obj))
            }

            override fun d(tag: String, msg: String, vararg obj: Any) {
                android.util.Log.d(tag, msg.format(*obj))
            }

            override fun printErrStackTrace(
                tag: String,
                tr: Throwable,
                format: String,
                vararg obj: Any
            ) {
                android.util.Log.e(tag, String.format(format, *obj), tr)
            }
        })
    }
}