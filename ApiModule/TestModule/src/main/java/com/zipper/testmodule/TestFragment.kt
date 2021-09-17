package com.zipper.testmodule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zipper.api.module.MLog
import java.lang.Exception

class TestFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            val pluginInflater = TestModuleImpl.instance!!.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            MLog.d("测试页 onCreateView >> pluginInflater = $pluginInflater")
            val view = pluginInflater.inflate(R.layout.test_module_fragment_main, container,false)
            MLog.d("测试页 onCreateView >> view = $view")
            return view
        }catch (e: Exception){
            e.printStackTrace()
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MLog.d("测试页 onViewCreated")

    }
}