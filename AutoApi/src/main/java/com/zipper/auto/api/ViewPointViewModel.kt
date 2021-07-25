package com.zipper.auto.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewPointViewModel: ViewModel() {

    private val viewList:MutableLiveData<List<ViewPoint>> by lazy {
        MutableLiveData<List<ViewPoint>>().also {
            loadLocalLiveData()
        }
    }

    fun getViewPoint(): LiveData<List<ViewPoint>> = viewList


    private fun loadLocalLiveData(){
        PluginAutoApi.jjsDatabase.getJJSDao().findUnStart()
    }
}