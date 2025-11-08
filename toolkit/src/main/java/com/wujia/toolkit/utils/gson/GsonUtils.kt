package com.wujia.toolkit.utils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Gson 工具类，封装 JSON 序列化与反序列化操作
 */
class GsonUtils private constructor() {

    // 私有构造函数，禁止实例化
    init {
        throw UnsupportedOperationException("Cannot instantiate GsonUtils")
    }

    companion object {
        /**
         * 获取 Gson 实例（用于自定义序列化/反序列化操作）
         *
         * @return Gson 实例
         */
        // 单例 Gson 实例（线程安全）
        // 初始化 Gson，可配置格式化输出、日期格式等
        private val gson: Gson = GsonBuilder()
            .setPrettyPrinting() // 格式化输出（可选，默认紧凑格式）
            .serializeNulls() // 序列化 null 值（默认不序列化）
            .disableHtmlEscaping() // 禁用 HTML 转义（如不转义 < > 等）
            // .setDateFormat("yyyy-MM-dd HH:mm:ss") // 自定义日期格式
            .create()

        /**
         * 将对象转换为 JSON 字符串
         *
         * @param obj 任意对象（实体类、集合等）
         * @return JSON 字符串，若对象为 null 则返回 "null"
         */
        fun toJson(obj: Any?): String? {
            if (obj == null) {
                return "null"
            }
            try {
                return gson.toJson(obj)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return ""
            }
        }

        /**
         * 将 JSON 字符串转换为指定类型的对象
         *
         * @param json  JSON 字符串
         * @param clazz 目标类的 Class
         * @param <T>   目标类型
         * @return 转换后的对象，失败则返回 null
         </T> */
        fun <T> fromJson(json: String?, clazz: Class<T?>?): T? {
            if (json == null || json.isEmpty()) {
                return null
            }
            try {
                return gson.fromJson<T?>(json, clazz)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 将 JSON 字符串转换为泛型对象（如 List<T>、Map<K></K>, V> 等）
         *
         * @param json  JSON 字符串
         * @param type  泛型类型（通过 TypeToken 获取，如 new TypeToken<List></List><T>>(){}.getType()）
         * @param <T>   目标类型
         * @return 转换后的泛型对象，失败则返回 null
         </T></T></T> */
        fun <T> fromJson(json: String?, type: Type?): T? {
            if (json == null || json.isEmpty()) {
                return null
            }
            try {
                return gson.fromJson<T?>(json, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * 将 JSON 字符串转换为 List 集合
         *
         * @param json  JSON 字符串
         * @param clazz 集合中元素的 Class
         * @param <T>   元素类型
         * @return List 集合，失败则返回 null
         </T> */
        fun <T> jsonToList(json: String?, clazz: Class<T>): MutableList<T>? {
            // 构建 List<T> 类型
            val type = TypeToken.getParameterized(MutableList::class.java, clazz).getType()
            return fromJson<MutableList<T>?>(json, type)
        }

        /**
         * 将 JSON 字符串转换为 Map 集合
         *
         * @param json     JSON 字符串
         * @param keyClazz 键的 Class
         * @param valClazz 值的 Class
         * @param <K>      键类型
         * @param <V>      值类型
         * @return Map 集合，失败则返回 null
         </V></K> */
        fun <K, V> jsonToMap(
            json: String?,
            keyClazz: Class<K?>,
            valClazz: Class<V?>?,
        ): MutableMap<K?, V?>? {
            // 构建 Map<K, V> 类型
            val type =
                TypeToken.getParameterized(MutableMap::class.java, keyClazz, valClazz).getType()
            return fromJson<MutableMap<K?, V?>?>(json, type)
        }
    }
}
