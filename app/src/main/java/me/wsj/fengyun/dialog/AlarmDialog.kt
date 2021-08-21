package me.wsj.fengyun.dialog

import android.content.Context
import me.wsj.fengyun.databinding.DialogAlarmBinding
import me.wsj.lib.dialog.BaseDialog

class AlarmDialog(context: Context) : BaseDialog<DialogAlarmBinding>(context, 0.66f, 0f) {

    override fun bindView() = DialogAlarmBinding.inflate(layoutInflater)

    override fun initView() {
        setCanceledOnTouchOutside(true)
    }

    fun setContent(content: String) {
        mBinding.tvContent.text = content
    }

    override fun initListener() {
        mBinding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}