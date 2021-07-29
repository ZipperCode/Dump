package com.zipper.core.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

object SpUtil {

    private lateinit var appContext: Context

    const val BASE_NAME = "sp_base"

    private val cacheSp: MutableMap<String, SharedPreferencesWrapper> = mutableMapOf()

    init {
        try {
            val forNameMethod = Class::class.java.getDeclaredMethod("forName", String::class.java)
            val getDeclareMethod = Class::class.java.getDeclaredMethod(
                "getDeclaredMethod",
                Class::class.java,
                *emptyArray()
            )
            val getDeclareFieldMethod =
                Class::class.java.getDeclaredMethod("getDeclaredField", Class::class.java)
            val clazz = forNameMethod.invoke(null, "android.app.ActivityThread") as Class<*>
            val atObj = getDeclareMethod.invoke(null, "currentActivityThread")
            val appField = getDeclareFieldMethod.invoke(clazz, "mInitialApplication") as Field
            appField.isAccessible = true
            appContext = appField.get(atObj) as Application
            get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun init(context: Context){
        this.appContext = context.applicationContext
    }

    fun get(spName: String): SharedPreferencesWrapper {
        if (cacheSp.containsKey(spName)) {
            return cacheSp[spName]!!
        }
        val spw = SharedPreferencesWrapper(appContext.getSharedPreferences(spName, Context.MODE_PRIVATE))
        cacheSp[spName] = spw
        return spw
    }

    fun get(): SharedPreferencesWrapper {
        if (cacheSp.containsKey(BASE_NAME)) {
            return cacheSp[BASE_NAME]!!
        }
        val spw =
            SharedPreferencesWrapper(appContext.getSharedPreferences(BASE_NAME, Context.MODE_PRIVATE))
        cacheSp[BASE_NAME] = spw
        return spw
    }

    fun put(key: String, value: Boolean) {
        cacheSp[BASE_NAME]!!.put(key, value)
    }

    class SharedPreferencesWrapper(val sp: SharedPreferences) {

        fun put(key: String, value: Boolean) {
            sp.edit().putBoolean(key, value).apply()
        }

        fun put(key: String, value: String) {
            sp.edit().putString(key, value).apply()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> get(key: String, default: T): T {
            return when (default) {
                is Boolean -> sp.getBoolean(key, default) as T
                is Int -> sp.getInt(key, default) as T
                is String -> sp.getString(key, default) as T
                is Long -> sp.getLong(key, default) as T
                is Float -> sp.getFloat(key,default) as T
                is Set<*> -> sp.getStringSet(key, emptySet<String>()) as T
                else -> throw IllegalArgumentException("unknown default param type")
            }
        }
    }


}