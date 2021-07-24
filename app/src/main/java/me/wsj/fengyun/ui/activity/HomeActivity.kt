package me.wsj.fengyun.ui.activity

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ViewPagerAdapter
import me.wsj.fengyun.databinding.ActivityMainBinding
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.expand
import me.wsj.fengyun.ui.activity.vm.MainViewModel
import me.wsj.fengyun.ui.base.BaseVmActivity
import me.wsj.fengyun.ui.fragment.WeatherFragment
import me.wsj.lib.EffectUtil
import me.wsj.lib.extension.startActivity
import me.wsj.lib.specialeffects.ICancelable
import me.wsj.lib.utils.IconUtils
import per.wsj.commonlib.utils.DisplayUtil
import java.util.*

class HomeActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    private val fragments: MutableList<Fragment> by lazy { ArrayList() }
    private val cityList = ArrayList<CityEntity>()
    private var mCurIndex = 0

    override fun bindView() = ActivityMainBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent) {}

    override fun initView() {
        hideTitleBar()
        //透明状态栏
        transparentStatusBar()

        mBinding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        mBinding.viewPager.offscreenPageLimit = 5

        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                mBinding.ivLoc.visibility =
                    if (cityList[i].isLocal) View.VISIBLE else View.INVISIBLE

                mBinding.llRound.getChildAt(mCurIndex).isEnabled = false
                mBinding.llRound.getChildAt(i).isEnabled = true
                mCurIndex = i
                mBinding.tvLocation.text = cityList[i].cityName
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

        mBinding.ivAddCity.expand(10, 10)

        mBinding.ivBg.setImageResource(IconUtils.getDefaultBg())
    }

    override fun initEvent() {
        mBinding.ivSetting.setOnClickListener { v ->
            startActivity<SettingsActivity>()
        }
        mBinding.ivAddCity.setOnClickListener { v ->
            startActivity<AddCityActivity>()
        }

        viewModel.mCities.observe(this) {
            if (it.isEmpty()) {
                startActivity<AddCityActivity>()
            } else {
                cityList.clear()
                cityList.addAll(it)
                showCity()
            }
        }

        viewModel.mCurCondCode.observe(this) {
            changeBg(it)
        }


    }

    override fun initData() {
        viewModel.getCities()

    }

    /**
     * 显示城市
     */
    private fun showCity() {
        if (mCurIndex > cityList.size - 1) {
            mCurIndex = cityList.size - 1
        }

        mBinding.ivLoc.visibility =
            if (cityList[mCurIndex].isLocal) View.VISIBLE else View.INVISIBLE
        mBinding.tvLocation.text = cityList[mCurIndex].cityName

        mBinding.llRound.removeAllViews()
        // 宽高参数
        val layoutParams = LinearLayout.LayoutParams(
            DisplayUtil.dip2px(this, 4f),
            DisplayUtil.dip2px(this, 4f)
        )
        //设置间隔
        layoutParams.rightMargin = 12

        for (i in cityList.indices) {
            //创建底部指示器(小圆点)
            val view = View(this@HomeActivity)
            view.setBackgroundResource(R.drawable.background)
            view.isEnabled = false

            //添加到LinearLayout
            mBinding.llRound.addView(view, layoutParams)
        }
        // 小白点
        mBinding.llRound.getChildAt(mCurIndex).isEnabled = true
        mBinding.llRound.visibility = if (cityList.size <= 1) View.GONE else View.VISIBLE

        fragments.clear()
        for (city in cityList) {
            val cityId = city.cityId
            val weatherFragment = WeatherFragment.newInstance(cityId)
            fragments.add(weatherFragment)
        }

        mBinding.viewPager.adapter?.notifyDataSetChanged()
        mBinding.viewPager.currentItem = mCurIndex
    }


    override fun onResume() {
        super.onResume()
        if (ContentUtil.CITY_CHANGE) {
            viewModel.getCities()
            ContentUtil.CITY_CHANGE = false
        }
    }

    fun changeBg(condCode: String) {
        // 获取背景
        val bgDrawable = IconUtils.getBg(this@HomeActivity, condCode.toInt())
        mBinding.ivBg.setImageResource(bgDrawable)

        // 获取特效
        val effectDrawable = EffectUtil.getEffect(context, condCode.toInt())
        mBinding.ivEffect.setImageDrawable(effectDrawable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.ivEffect.drawable?.let {
            (it as ICancelable).cancel()
        }
    }
}