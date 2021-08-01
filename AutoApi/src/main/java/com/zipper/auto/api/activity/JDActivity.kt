package com.zipper.auto.api.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.jd.api.JdNecklace
import com.zipper.auto.api.R
import com.zipper.auto.api.script.ScriptManager
import com.zipper.core.activity.BaseVmActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JDActivity: BaseVmActivity<JDViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jd)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO){
//                            ScriptManager.getScript(this@JDActivity,"Fuc")
                            doAction()
                        }
                    }
                } else {
                    Toast.makeText(this, "需要权限", Toast.LENGTH_SHORT).show()
                }
            }

        findViewById<Button>(R.id.button).setOnClickListener {
            lifecycleScope.launch {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    when {
                        ContextCompat.checkSelfPermission(
                            this@JDActivity,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            withContext(Dispatchers.IO){
//                                ScriptManager.getScript(this@JDActivity,"Fuc")
                                doAction()
                            }
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
    }

    private fun doAction(){
        lifecycleScope.launch{
            JdNecklace().main(this@JDActivity)
        }
    }
}