package com.zipper.dump

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class MainViewModel: ViewModel() {

    val kgState:MutableLiveData<Boolean> = MutableLiveData(false)
    val zqState:MutableLiveData<Boolean> = MutableLiveData(false)
    val jcState:MutableLiveData<Boolean> = MutableLiveData(false)
    val gdState:MutableLiveData<Boolean> = MutableLiveData(false)
}