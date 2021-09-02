package com.zipper.auto.api.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import com.zipper.auto.api.R

class ApiViewModel: ViewModel() {

    companion object{
        val mainFragmentSet = setOf(R.id.nav_home_fragment, R.id.nav_variable_fragment, R.id.nav_person_fragment)
    }

    private val _navBottomBarVisible: MutableLiveData<Boolean> = MutableLiveData(true)

    val navBottomBarVisible: LiveData<Boolean> get() = _navBottomBarVisible

    fun destinationChanged(destination: NavDestination){
        _navBottomBarVisible.value = containMainScreen(destination.id)
    }

    fun containMainScreen(id: Int?) : Boolean = mainFragmentSet.contains(id)
}