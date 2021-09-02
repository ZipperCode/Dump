package com.zipper.auto.api.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.databinding.ActivityApiBinding
import com.zipper.core.activity.BaseVmBActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiActivity : BaseVmBActivity<ApiViewModel, ActivityApiBinding>() {

    companion object{
        const val TAG: String = "JDActivity"
    }
    
    private lateinit var navController: NavController

    override fun vmBrId(): Int = BR.vm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_bottom_view)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mBaseViewModel.destinationChanged(destination)
        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    doAction()
                } else {
                    Toast.makeText(this, "需要权限", Toast.LENGTH_SHORT).show()
                }
            }

        findViewById<Button>(R.id.button)?.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                when {
                    ContextCompat.checkSelfPermission(
                        this@ApiActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        doAction()
                    }
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        showToast("shouldShowRequestPermissionRationale")
                    }
                    else -> {
                        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        if(mBaseViewModel.containMainScreen(navController.currentDestination?.id)){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    private fun doAction() {
        lifecycleScope.launch {
//            JdNecklace().main(this@JDActivity)
            withContext(Dispatchers.IO){
//                ScriptManager.getScript(this@JDActivity,"Fuc")

            }
        }
    }


}