package me.wsj.fengyun.ui.activity

import android.content.Intent
import me.wsj.fengyun.R
import me.wsj.fengyun.databinding.ActivityAboutBinding
import me.wsj.fengyun.ui.base.BaseActivity
import per.wsj.commonlib.utils.CommonUtil

class AboutActivity : BaseActivity<ActivityAboutBinding>() {
    override fun bindView(): ActivityAboutBinding {
        return ActivityAboutBinding.inflate(layoutInflater)
    }

    override fun prepareData(intent: Intent?) {

    }

    override fun initView() {
        setTitle(getString(R.string.about))
        mBinding.tvVersionNum.text = CommonUtil.getVersionName(this)
    }

    override fun initEvent() {

    }

    override fun initData() {

    }
}