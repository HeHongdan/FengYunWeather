package me.wsj.fengyun.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.wsj.fengyun.R
import me.wsj.fengyun.databinding.ActivityAutoBootBinding
import me.wsj.fengyun.ui.base.BaseActivity

class AutoBootActivity : BaseActivity<ActivityAutoBootBinding>() {

    override fun bindView(): ActivityAutoBootBinding {
        return ActivityAutoBootBinding.inflate(layoutInflater)
    }

    override fun prepareData(intent: Intent?) {
        //To do sth.
    }

    override fun initView() {
        setTitle("提示")
    }

    override fun initEvent() {
        //To do sth.
    }

    override fun initData() {
        //To do sth.
    }
}