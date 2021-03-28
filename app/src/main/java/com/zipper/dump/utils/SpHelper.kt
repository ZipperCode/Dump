package com.zipper.dump.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("StaticFieldLeak")
object SpHelper {

    private lateinit var mContext: Context

    private const val SP_NAME = "sp_dump"
    private const val SP_NAME_2 = "com.zipper.dump_preference"

    /**
     * 服务开关状态的值
     */
    const val SP_SERVICE_STATUS_KEY = "service_status"

    /**
     * 是否是首次打开应用，false - 首次打开，true - 非首次
     */
    const val SP_FIRST_OPENED_KEY = "first_opened"

    const val SETTING_WEIXIN_KEY = "setting_weixin"

    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mSharedPreferences_2: SharedPreferences

    private var isInit: Boolean = false

    @JvmStatic
    fun init(context: Context) {
        mContext = context.applicationContext
        mSharedPreferences = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        mSharedPreferences_2 = mContext.getSharedPreferences(SP_NAME_2, Context.MODE_PRIVATE)
        isInit = true
    }

    fun saveString(key: String, value: String) {
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        val edit = mSharedPreferences.edit()
        edit.putString(key, value).apply()
    }

    fun loadString(key: String): String {
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        return mSharedPreferences.getString(key, "")!!
    }

    fun saveBoolean(key: String, value: Boolean = false){
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        val edit = mSharedPreferences.edit()
        edit.putBoolean(key, value).apply()
    }

    fun loadBoolean(key: String):Boolean{
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        return mSharedPreferences.getBoolean(key, false)
    }

    fun saveStringArray(key: String, values: Set<String>) {
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        val edit = mSharedPreferences.edit()
        edit.putStringSet(key, values).apply()
    }


    fun loadStringArray(key: String): Set<String> {
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        return mSharedPreferences.getStringSet(key, HashSet())!!
    }

    fun saveSettingBoolean(key: String, value: Boolean){
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        val edit = mSharedPreferences_2.edit()
        edit.putBoolean(key, value).apply()
    }

    fun loadSettingBoolean(key: String):Boolean{
        if (!isInit) {
            error("SpHelper must be call init method")
        }
        return mSharedPreferences_2.getBoolean(key, false)
    }
}