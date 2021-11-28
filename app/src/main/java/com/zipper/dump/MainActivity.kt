package com.zipper.dump

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.SparseArray
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.core.fragment.FragmentNavConfigHelper
import com.zipper.core.utils.SpUtil
import com.zipper.dump.databinding.AppActivityMainBinding

/**
 *  @author zipper
 *  @date 2021-07-30
 *  @description
 **/
class MainActivity: BaseVmBActivity<MainViewModel,AppActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        FragmentNavConfigHelper
            .addTopFragmentId(R.id.nav_home_fragment)
            .addTopFragmentId(R.id.nav_variable_fragment)
            .addTopFragmentId(R.id.nav_person_fragment)
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_bottom_view)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mBaseViewModel.destinationChanged(destination)
        }


    }

    override fun onBackPressed() {
        if (FragmentNavConfigHelper.isTopLevelId(navController.currentDestination?.id)) {
            finish()
        } else {
            super.onBackPressed()
        }

        super.onBackPressed()
    }

}