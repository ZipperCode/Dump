package com.zipper.dump.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.zipper.core.delegates.ViewById
import com.zipper.core.dialog.BaseBindDialog
import com.zipper.core.model.binding.ObservableString
import com.zipper.dump.BR
import com.zipper.dump.R
import com.zipper.dump.bean.VariableItemBean
import com.zipper.dump.databinding.DialogEditVariableBinding

class VariableEditDialog: BaseBindDialog<DialogEditVariableBinding>() {
    override fun layoutId(): Int = R.layout.dialog_edit_variable

    private var isNew: Boolean = false

    private var variableItemBean: VariableItemBean? = null

    private val ivClose: ImageView by ViewById(R.id.iv_close)

    private val btnSave: Button by ViewById(R.id.btn_save)

    private var onSave: ((VariableItemBean) -> Unit)? = null

    override fun getBindVariable(): SparseArray<Any> {

        if(variableItemBean == null){
            variableItemBean = VariableItemBean(ObservableString(), ObservableString())
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
        btnSave.setOnClickListener {
            onSave?.invoke(variableItemBean!!)
            dismiss()
        }
    }

    override fun onDestroyView() {
        onSave = null
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        onSave = null
        super.onDismiss(dialog)
    }

    companion object{

        @JvmStatic
        fun showDialog(activity: FragmentActivity, variableItemBean: VariableItemBean?,
                       onSave: ((VariableItemBean) -> Unit)? = null){
            val dialog = VariableEditDialog()
            dialog.variableItemBean = variableItemBean
            dialog.onSave = onSave
            dialog.show(activity.supportFragmentManager, VariableItemBean::javaClass.name)
        }
    }
}