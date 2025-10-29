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

import android.annotation.SuppressLint
import android.content.Context

class HiAppGlobal {

    companion object {
        // 使用反射获取全局Context
        @SuppressLint("PrivateApi")
        fun getApplication(): Context {
            try {
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                val currentApplicationMethod =
                    activityThreadClass.getDeclaredMethod("currentApplication")
                return currentApplicationMethod.invoke(null) as Context
            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw NullPointerException("Reflect application failed.")
        }

    }

}