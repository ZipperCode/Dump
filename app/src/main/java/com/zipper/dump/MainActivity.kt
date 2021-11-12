package com.zipper.dump

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.SparseArray
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.*

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

        SignService.kgState.observe(this, Observer {
            mBaseViewModel.kgState.value = it
        })

        SignService.zqState.observe(this, Observer {
            mBaseViewModel.zqState.value = it
        })
        SignService.jcState.observe(this, Observer {
            mBaseViewModel.jcState.value = it
        })

        SignService.gdState.observe(this, Observer {
            mBaseViewModel.gdState.value = it
        })
    }

    private fun startService(action: String){
        val intent = Intent(this@MainActivity, SignService::class.java)
        intent.action = action
        startService(intent)
    }

    inner class EventHandler{

        fun start(){
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        fun doKG(){
            if (mBaseViewModel.kgState.value == true){
                showToast("已经正在执行了")
                return
            }
            startService(SignService.KUGOU)
        }

        fun doZQ(){
            if (mBaseViewModel.zqState.value == true){
                showToast("已经正在执行了")
                return
            }
            startService(SignService.ZQKD)
        }

        fun doJC(){
            if (mBaseViewModel.jcState.value == true){
                showToast("已经正在执行了")
                return
            }
            startService(SignService.JCKD)
        }

        fun doGD(){
            if (mBaseViewModel.gdState.value == true){
                showToast("已经正在执行了")
                return
            }
            startService(SignService.GUBH)
        }
    }
}