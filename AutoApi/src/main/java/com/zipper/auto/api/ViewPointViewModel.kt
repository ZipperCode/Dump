package com.zipper.auto.api

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zipper.auto.api.bean.TitleBean
import com.zipper.auto.api.bean.ViewPoint
import com.zipper.base.service.plugin.impl.AutoApiPao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ViewPointViewModel: ViewModel() {

    val _titleData: MutableLiveData<List<TitleBean>> by lazy {
        MutableLiveData<List<TitleBean>>().also {
            loadTitle()
        }
    }

    private val viewList:MutableLiveData<List<ViewPoint>> by lazy {
        MutableLiveData<List<ViewPoint>>().also {
            loadLocalLiveData()
        }
    }

    fun getViewPoint(): LiveData<List<ViewPoint>> = viewList

    fun getTitleData(): LiveData<List<TitleBean>> = _titleData

    private fun loadTitle(){
        viewModelScope.launch {
            val findNowDayAllFlow = withContext(Dispatchers.IO){
                PluginAutoApi.jjsDatabase.getBaseJJSDao().findAllExpert()
            }
            findNowDayAllFlow.collect { exportData ->
                val list = exportData.map { TitleBean("${it.exportId}", ObservableBoolean(false)) }.toList()
                _titleData.value = list
            }
        }
    }

    fun loadLocalLiveData(){
        viewModelScope.launch {
            val findNowDayAllFlow = withContext(Dispatchers.IO){
                PluginAutoApi.jjsDatabase.getJJSDao().findAll()

            }
            findNowDayAllFlow.collect {
                viewList.value = it
            }
        }
    }

    fun loadByExpertId(id: String){
        viewModelScope.launch {
            val findByExpertId = withContext(Dispatchers.IO){
                PluginAutoApi.jjsDatabase.getBaseJJSDao().findByExpertId(id)
            }
            viewList.value = findByExpertId
        }
    }
}