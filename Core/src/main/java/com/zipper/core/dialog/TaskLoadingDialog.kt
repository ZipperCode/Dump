package com.zipper.core.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.zipper.core.R
import com.zipper.core.utils.L
import kotlinx.coroutines.Job
import java.lang.Exception

/**
 *  @author zipper
 *  @date 2021-08-09
 *  @description
 **/
class TaskLoadingDialog : DialogFragment(), DialogInterface.OnKeyListener {

    private var _taskJob: Job? = null

    private var _canCancel: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            window!!.decorView.apply {
                background = ColorDrawable(Color.TRANSPARENT)
                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            window!!.apply {
                val lp = attributes
                lp.dimAmount = 0.5f
                attributes = lp
            }
            setOnKeyListener(this@TaskLoadingDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.common_state_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(dialog?.window!!, true)
    }

    override fun onDestroy() {
        if(_taskJob?.isActive == true){
            try{
                _taskJob?.cancel()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onDestroy()
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        val resultStatus = _canCancel && keyCode == KeyEvent.KEYCODE_BACK
        if(resultStatus){
            _taskJob?.cancel()
            _taskJob = null
        }
        return keyCode == KeyEvent.KEYCODE_BACK && !_canCancel
    }


    fun withTaskJob(job: Job): TaskLoadingDialog{
        this._taskJob = job
        return this
    }

    fun canCancel(canCancel: Boolean): TaskLoadingDialog{
        this._canCancel = canCancel
        return this
    }

    fun show(activity: FragmentActivity){
        try {
            show(activity.supportFragmentManager, this.javaClass.name)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun release(){
        try {
            dismissAllowingStateLoss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


}