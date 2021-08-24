package com.zipper.dump.activity.test

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.zipper.dump.BR

/**
 *  @author zipper
 *  @date 2021-08-20
 *  @description
 **/
class ViewStubTestViewModel: ViewModel() {

    val contentData: MutableLiveData<String> = MutableLiveData("我是实体内容")

    val subBind: SparseArray<Any> by lazy {
        SparseArray<Any>().apply {
            put(BR.vm, this@ViewStubTestViewModel)
        }
    }

    fun changeContent(){
        contentData.value = "我是修改后的实体内容 ${Math.random()}"
    }

}