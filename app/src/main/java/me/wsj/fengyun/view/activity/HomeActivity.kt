package me.wsj.fengyun.view.activity

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ViewPagerAdapter
import me.wsj.fengyun.databinding.ActivityMainBinding
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.extension.startActivity
import me.wsj.fengyun.utils.IconUtils
import me.wsj.fengyun.view.base.BaseActivity
import me.wsj.fengyun.view.fragment.WeatherFragment
import me.wsj.fengyun.view.fragment.WeatherViewModel
import org.joda.time.DateTime
import per.wsj.commonlib.utils.DisplayUtil
import java.util.*

class HomeActivity : BaseActivity<ActivityMainBinding>() {

    private val fragments: MutableList<Fragment> by lazy { ArrayList() }

    private var mCurIndex = 0

    private val viewModel: WeatherViewModel by viewModels()

    override fun bindView() = ActivityMainBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent) {

    }

    override fun initView() {
        hideTitleBar()
        //透明状态栏
        transparentStatusBar()
    }

    override fun initEvent() {
        mBinding.ivSetting.setOnClickListener { v ->
            startActivity<SettingsActivity>()
        }
        mBinding.ivAddCity.setOnClickListener { v ->
            startActivity<SearchActivity>()
        }

        viewModel.mCities.observe(this) {
            if (it.isEmpty()) {
                startActivity<SearchActivity>()
            } else {
                showCity(it)
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
    private fun showCity(cityList: List<CityEntity>) {
        if (mCurIndex > cityList.size - 1) {
            mCurIndex = cityList.size - 1
        }

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

        fragments.clear()
        for (city in cityList) {
            val cityId = city.cityId
            val weatherFragment = WeatherFragment.newInstance(cityId)
            fragments.add(weatherFragment)
        }

        mBinding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)


        mBinding.llRound.visibility = if (fragments.size == 1) View.GONE else View.VISIBLE
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
        mBinding.viewPager.currentItem = mCurIndex
        mBinding.tvLocation.text = cityList[mCurIndex].cityName
    }


    override fun onResume() {
        super.onResume()

    }

    fun changeBg(condCode: String) {
        val nowTime = DateTime.now()
        val hourOfDay = nowTime.hourOfDay
        if (hourOfDay in 7..18) {
            mBinding.ivBg.setImageResource(IconUtils.getDayBack(this@HomeActivity, condCode))
        } else {
            mBinding.ivBg.setImageResource(IconUtils.getNightBack(this@HomeActivity, condCode))
        }
    }
}
