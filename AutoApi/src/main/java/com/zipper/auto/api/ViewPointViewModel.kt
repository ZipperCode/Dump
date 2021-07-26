package com.zipper.auto.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.auto.api.bean.ViewPoint
import com.zipper.base.service.plugin.impl.AutoApiPao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ViewPointViewModel: ViewModel() {

    init {
        AutoApiPao.fetchData()
    }

    private val viewList:MutableLiveData<List<ViewPoint>> by lazy {
        MutableLiveData<List<ViewPoint>>().also {
            loadLocalLiveData()
        }
    }

    fun getViewPoint(): LiveData<List<ViewPoint>> = viewList

    private fun loadLocalLiveData(){
        viewModelScope.launch {
            val findNowDayAllFlow = withContext(Dispatchers.IO){
                PluginAutoApi.jjsDatabase.getJJSDao().findAll()

            }
            findNowDayAllFlow.collect {
                viewList.value = it
            }
        }

    }
}