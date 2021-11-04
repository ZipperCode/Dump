package com.zipper.dump

import android.os.Bundle
import android.util.SparseArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.core.utils.SpUtil
import com.zipper.dump.databinding.MainBinding
import com.zipper.sign.core.base.BaseApi
import com.zipper.sign.gdbh.GdbhApi
import com.zipper.sign.jckd.JckdApi
import com.zipper.sign.jckd.JckdApiParam
import com.zipper.sign.kugou.ConfigBean
import com.zipper.sign.kugou.KgTaskApi
import com.zipper.sign.zqkd.ZqkdApi
import com.zipper.sign.zqkd.ZqkdApiParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class MainActivity: BaseVmBActivity<MainViewModel,MainBinding>() {

    companion object{
        const val TASK_SP_NAME = "task_config"
    }

    override fun vmBrId(): Int = BR.vm

    private val mRunTaskApi: MutableList<BaseApi> = mutableListOf()

    override fun getVariable(): SparseArray<Any> {
        return super.getVariable().apply {
            put(BR.handler,EventHandler())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isReloadConfig = SpUtil.instance(TASK_SP_NAME).get(Constant.SP_KEY_IS_RE_LOAD_TASK_CONFIG, true)
        if (isReloadConfig){
            assets.open("task.json").use {
                val data:Map<String, String> = Gson().fromJson(it.reader(),object :TypeToken<Map<String, String>>(){}.type)
                data.forEach { (k, v) ->
                    SpUtil.instance(TASK_SP_NAME).putAsync(k, v)
                }
            }
            SpUtil.instance(TASK_SP_NAME).put(Constant.SP_KEY_IS_RE_LOAD_TASK_CONFIG, false)
        }
    }

    inner class EventHandler{

        fun doKG(){
            if (mBaseViewModel.kgState.value == true){
                showToast("已经正在执行了")
                return
            }

            mBaseViewModel.kgState.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val json = SpUtil.instance(TASK_SP_NAME).get(Constant.SP_KEY_KG,"[]")
                val userList: List<ConfigBean> = Gson().fromJson(json, object : TypeToken<List<ConfigBean>>(){}.type)
                userList.map {
                    async {
                        val task = KgTaskApi(it)
                        mRunTaskApi.add(task)
                        task.execute()
                        task
                    }
                }.forEach {
                    val res = it.await()
                    mRunTaskApi.remove(res)
                }
                mBaseViewModel.kgState.value = false
            }
        }

        fun doZQ(){
            if (mBaseViewModel.zqState.value == true){
                showToast("已经正在执行了")
                return
            }
            mBaseViewModel.zqState.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val json = SpUtil.instance(TASK_SP_NAME).get(Constant.SP_KEY_ZQ,"[]")
                val list: List<ZqkdApiParam> = Gson().fromJson(json, object : TypeToken<List<ZqkdApiParam>>() {}.type)
                list.map {
                    async {
                        val task = ZqkdApi()
                        mRunTaskApi.add(task)
                        task.execute(it)
                        task
                    }
                }.forEach {
                    val res = it.await()
                    mRunTaskApi.remove(res)
                }
                mBaseViewModel.zqState.value = false
            }

        }

        fun doJC(){
            if (mBaseViewModel.jcState.value == true){
                showToast("已经正在执行了")
                return
            }
            mBaseViewModel.jcState.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val json = SpUtil.instance(TASK_SP_NAME).get(Constant.SP_KEY_JC,"[]")
                val list: List<JckdApiParam> = Gson().fromJson(json, object : TypeToken<List<JckdApiParam>>() {}.type)
                list.map {
                    async {
                        val task = JckdApi()
                        mRunTaskApi.add(task)
                        task.execute(it)
                        task
                    }
                }.forEach {
                    val res = it.await()
                    mRunTaskApi.remove(res)
                }
                mBaseViewModel.jcState.value = false
            }
        }

        fun doGD(){
            if (mBaseViewModel.gdState.value == true){
                showToast("已经正在执行了")
                return
            }
            mBaseViewModel.gdState.value = true

            CoroutineScope(Dispatchers.IO).launch {
                GdbhApi().testExecute()
                mBaseViewModel.gdState.value = false
            }
        }
    }
}