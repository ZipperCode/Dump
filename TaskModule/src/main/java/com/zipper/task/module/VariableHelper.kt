package com.zipper.task.module

import android.text.TextUtils
import com.google.gson.Gson
import com.zipper.core.utils.L
import com.zipper.core.utils.SpUtil
import com.zipper.task.module.bean.VariableBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VariableHelper {
    private const val TAG = "VariableHelper"
    
    private const val SP_NAME_VARIABLE = "sp_variable"
    private const val SP_NAME_VARIABLE_KEYS = "sp_variable_keys"

    /**
     * 存储变量所有key的键
     */
    private const val SP_KEY_VARIABLE_KEYS = "variable_keys"

    private val gson = Gson()

    /**
     * 保存指定变量
     */
    suspend fun saveVariable(variable: VariableBean) = withContext(Dispatchers.IO){
        if (!variable.check()){
            L.d(TAG,"==saveVariable== key - value check failure")
            return@withContext
        }

        val variableKeys = HashSet(getVariableKeys())
        variableKeys.add(variable.name)
        SpUtil.instance(SP_NAME_VARIABLE).put(variable.name, gson.toJson(variable))
        SpUtil.instance(SP_NAME_VARIABLE_KEYS).put(SP_KEY_VARIABLE_KEYS, variableKeys)
    }

    /**
     * 更新变量值
     */
    suspend fun updateVariable(variable: VariableBean) = withContext(Dispatchers.IO){
        if (!variable.check()){
            L.d(TAG,"==saveVariable== key - value check failure")
            return@withContext
        }
        if (!checkVariableExists(variable.name)){
            L.d(TAG,"==updateVariable== key - value check value is null or empty")
            return@withContext
        }
        SpUtil.instance(SP_NAME_VARIABLE).put(variable.name, gson.toJson(variable))
    }

    /**
     * 删除指定key的值
     */
    suspend fun deleteVariable(key: String)= withContext(Dispatchers.IO){
        if (TextUtils.isEmpty(key)){
            return@withContext
        }
        val variableKeys = getVariableKeys()
        if (variableKeys.remove(key)){
            SpUtil.instance(SP_NAME_VARIABLE_KEYS).put(SP_KEY_VARIABLE_KEYS, variableKeys)
        }
        SpUtil.instance(SP_NAME_VARIABLE).remove(key)
    }

    /**
     * 获取指定key的变量
     */
    suspend fun getVariable(key: String): VariableBean? = withContext(Dispatchers.IO){
        val value = SpUtil.instance(SP_NAME_VARIABLE).get(key, "")
        if(TextUtils.isEmpty(value)){
            return@withContext null
        }
        return@withContext Gson().fromJson(value, VariableBean::class.java)
    }

    /**
     * 获取存储的变量key列表
     */
    suspend fun getVariableList(): List<VariableBean> = withContext(Dispatchers.IO){
        val variableKeys = getVariableKeys()
        val list = mutableListOf<VariableBean>()
        var needUpdate = false

        val iterator = variableKeys.iterator()
        while (iterator.hasNext()){
            val key = iterator.next()
            val value = SpUtil.instance(SP_NAME_VARIABLE).get(key, "")
            // 顺便去除不存在值的变量
            if(TextUtils.isEmpty(value)){
                needUpdate = true
                iterator.remove()
                continue
            }
            val variable = gson.fromJson(value, VariableBean::class.java)
            list.add(variable)
        }

        if(needUpdate){
            updateVariableKeys(variableKeys)
        }
        return@withContext list
    }

    private suspend fun updateVariableKeys(variableKeys: Set<String>) = withContext(Dispatchers.IO){
        SpUtil.instance(SP_NAME_VARIABLE_KEYS).put(SP_KEY_VARIABLE_KEYS, variableKeys)
    }

    suspend fun checkVariableExists(key: String): Boolean = withContext(Dispatchers.IO){
        return@withContext getVariableKeys().contains(key)
    }

    private suspend fun getVariableKeys(): MutableSet<String> = withContext(Dispatchers.IO){
        return@withContext SpUtil.instance(SP_NAME_VARIABLE_KEYS).get(SP_KEY_VARIABLE_KEYS, mutableSetOf<String>())
    }



}