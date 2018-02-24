package com.yadaniil.bitcurve.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

/**
 * Created by danielyakovlev on 1/10/18.
 */


object JsonUtils {

    //region Helpers
    private val gson: Gson
        get() = Gson()

    fun <T> toJson(value: T): String {
        val gson = gson
        return gson.toJson(value)
    }

    /**
     * Parse json
     * @return parsed object or `null` if `json` is invalid, empty or null
     */
    fun <T> fromJson(json: String, type: Class<T>): T? {
        return fromJson(json, type, null)
    }

    /**
     * Parse json
     * @return parsed object or new instance of this object if json is invalid, empty or null
     */
    fun <T> fromJson(json: String, type: Type): T? {
        return fromJson<T>(json, type, null)
    }

    fun <T> fromJson(json: String, type: Class<T>, defValue: T?): T? {
        return fromJson(json, type as Type, defValue)
    }

    fun <T> fromJson(json: String, type: Type, defValue: T?): T? {
        val gson = gson

        if (json.isEmpty())
            return defValue

        return try {
            gson.fromJson<T>(json, type)
        } catch (e: JsonSyntaxException) {
            defValue
        }

    }
    //endregion Helpers
}