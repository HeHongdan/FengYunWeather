package me.wsj.fengyun.view.activity

import android.content.Intent
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ControlCityAdapter
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.CityBeanList
import me.wsj.fengyun.databinding.ActivityControlCityBinding
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.SpUtils
import me.wsj.fengyun.view.base.BaseActivity

class CityControlActivity : BaseActivity<ActivityControlCityBinding>() {

    private var datas: List<CityBean> = ArrayList()

    override fun bindView() = ActivityControlCityBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {
    }

    override fun initView() {
        setTitle(getString(R.string.control_city))

        val key = "cityBean"

        var cityBeanList = SpUtils.getBean(
            this, key,
            CityBeanList::class.java
        )
        cityBeanList?.let {
            datas = it.cityBeans
        }
        val followCityAdapter = ControlCityAdapter(this@CityControlActivity, datas)

        mBinding.recycleControl.adapter = followCityAdapter
    }

    override fun initEvent() {
    }

    override fun initData() {
    }
}