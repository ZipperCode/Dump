package com.zipper.api.module.activity

import android.os.Bundle
import com.zipper.api.module.ApiModuleManager
import com.zipper.api.module.R
import com.zipper.core.activity.BaseVmActivity

class ModuleActivity: BaseVmActivity<ModuleViewModel>() {

    companion object{
        const val API_MODULE_KEY = "API_MODULE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_module)

        val apiModuleKey = intent.getStringExtra(API_MODULE_KEY) ?: ""

        if (apiModuleKey.isEmpty()){
            showToast("需要设置模块名称")
            finish()
        }

        val apiModule = ApiModuleManager.getModuleByKey(apiModuleKey)

        if (apiModule == null){
            showToast("找不到${apiModuleKey}模块")
            return
        }

        val mainFragment = apiModule.getMainFragment()

        if (mainFragment == null){
            showToast("未实现mainFragment")
            return
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.module_container, mainFragment, "Main")
            .commit()
    }



}