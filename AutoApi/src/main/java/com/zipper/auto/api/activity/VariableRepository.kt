package com.zipper.auto.api.activity

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.api.module.bean.ApiVariableBean
import com.zipper.core.utils.L
import com.zipper.core.utils.SpUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.subscribeOn
import kotlinx.coroutines.withContext

class VariableRepository {

    companion object{
        const val TAG = "VariableRepository"

        const val SP_VARIABLE_NAME = "SP_VARIABLE_NAME"
        const val SP_VARIABLE_KEY = "SP_VARIABLE_KEY"
    }

    /**
     * use io
     */
    val apiVariableBeanFlow: Flow<List<ApiVariableBean>> = flow {
        emit(getVariableList())
    }.flowOn(Dispatchers.IO)

    suspend fun getVariable(key: String): ApiVariableBean? = withContext(Dispatchers.IO){
        val value = SpUtil.instance(SP_VARIABLE_NAME).get(key, "")
        if(TextUtils.isEmpty(value)){
            return@withContext null
        }
        return@withContext Gson().fromJson(value, ApiVariableBean::class.java)
    }

    private suspend fun getVariableList(): List<ApiVariableBean> = withContext(Dispatchers.IO){
        val variableKeys = getVariableKeys()
        val gson = Gson()
        val list = mutableListOf<ApiVariableBean>()
        var needUpdate = false

        val iterator = variableKeys.iterator()
        while (iterator.hasNext()){
            val key = iterator.next()
            val value = SpUtil.instance(SP_VARIABLE_NAME).get(key, "")
            if(TextUtils.isEmpty(value)){
                needUpdate = true
                iterator.remove()
                continue
            }
            val variable = gson.fromJson(value, ApiVariableBean::class.java)
            list.add(variable)
        }

        if(needUpdate){
            updateVariableKeys(variableKeys)
        }
        return@withContext list
    }

    private suspend fun updateVariableKeys(variableKeys: Set<String>) = withContext(Dispatchers.IO){
        SpUtil.instance(SP_VARIABLE_NAME).put(SP_VARIABLE_KEY, variableKeys)
    }

    suspend fun checkVariableExists(key: String): Boolean = withContext(Dispatchers.IO){
        return@withContext SpUtil.instance(SP_VARIABLE_NAME).get(key, "").isNotEmpty()
    }

    private suspend fun getVariableKeys(): MutableSet<String> = withContext(Dispatchers.IO){
        return@withContext SpUtil.instance(SP_VARIABLE_NAME).get(SP_VARIABLE_KEY, mutableSetOf<String>())
    }

    suspend fun saveVariable(variable: ApiVariableBean) = withContext(Dispatchers.IO){
        if (!variable.check()){
            L.d(TAG,"==saveVariable== key - value check failure")
            return@withContext
        }
        val json = Gson().toJson(variable)
        val variableKeys = HashSet<String>(getVariableKeys())
        variableKeys.add(variable.name)
        SpUtil.instance(SP_VARIABLE_NAME).put(variable.name, json)
        SpUtil.instance(SP_VARIABLE_NAME).put(SP_VARIABLE_KEY, variableKeys)
    }
}