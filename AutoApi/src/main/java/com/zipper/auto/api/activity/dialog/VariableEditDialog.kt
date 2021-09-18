package com.zipper.auto.api.activity.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import com.zipper.auto.api.BR
import com.zipper.auto.api.R
import com.zipper.auto.api.activity.bean.VariableItemBean
import com.zipper.auto.api.databinding.DialogEditVariableBinding
import com.zipper.core.delegates.ViewById
import com.zipper.core.dialog.BaseBindDialog

class VariableEditDialog: BaseBindDialog<DialogEditVariableBinding>() {
    override fun layoutId(): Int = R.layout.dialog_edit_variable

    private var isNew: Boolean = false

    private var variableItemBean: VariableItemBean? = null

    private val ivClose: ImageView by ViewById(R.id.iv_close)

    private var onDismiss: ((VariableItemBean) -> Unit)? = null

    override fun getBindVariable(): SparseArray<Any> {

        if(variableItemBean == null){
            variableItemBean = VariableItemBean(ObservableField(""), ObservableField(""))
            isNew = true
        }

        return super.getBindVariable().apply {
            put(BR.bean, variableItemBean)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivClose.bringToFront()
        ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismiss?.invoke(variableItemBean!!)
        onDismiss = null
        super.onDismiss(dialog)
    }

    companion object{

        @JvmStatic
        fun showDialog(activity: FragmentActivity, variableItemBean: VariableItemBean?, onDismiss: ((VariableItemBean) -> Unit)? = null){
            val dialog = VariableEditDialog()
            dialog.variableItemBean = variableItemBean
            dialog.onDismiss = onDismiss
            dialog.show(activity.supportFragmentManager, VariableItemBean::javaClass.name)
        }
    }
}