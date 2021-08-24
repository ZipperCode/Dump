package com.zipper.dump.activity.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.adapters.ViewStubBindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zipper.core.activity.BaseVmBActivity
import com.zipper.core.view.StateLayout
import com.zipper.dump.R
import com.zipper.dump.BR
import com.zipper.dump.databinding.TestActivityViewStubBinding
import kotlin.random.Random

/**
 *  @author zipper
 *  @date 2021-08-19
 *  @description
 **/
class ViewStubTestActivity: BaseVmBActivity<ViewStubTestViewModel,TestActivityViewStubBinding>() {

    var state = arrayOf(StateLayout.LAYOUT_LOADING_ID, StateLayout.LAYOUT_CONTENT_ID, StateLayout.LAYOUT_EMPTY_DATA_ID, StateLayout.LAYOUT_ERROR_ID)

    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity_view_stub)

        val floatButton: FloatingActionButton = findViewById(R.id.floatingActionButton)
        val stateLayout: StateLayout = findViewById(R.id.stateLayout)
        floatButton.setOnClickListener {
            val random = Random(4)
            stateLayout.changeViewState(state[index++ % state.size])
        }
    }

    override fun vmBrId(): Int = BR.vm
}