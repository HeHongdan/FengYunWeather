package me.wsj.fengyun.ui.activity

import android.content.Intent
import android.graphics.drawable.Animatable
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ViewPagerAdapter
import me.wsj.fengyun.databinding.ActivityMainBinding
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.utils.expand
import me.wsj.fengyun.ui.activity.vm.MainViewModel
import me.wsj.fengyun.ui.base.BaseVmActivity
import me.wsj.fengyun.ui.fragment.WeatherFragment
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.lib.EffectUtil
import me.wsj.lib.extension.startActivity
import me.wsj.lib.utils.IconUtils
import per.wsj.commonlib.utils.DisplayUtil
import java.util.*

/**
 * 类描述：天气详情。
 *
 * @author HeHongdan
 * @date 2021/9/24
 * @since v2021/9/24
 */
class HomeActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    /** 城市界面的集合。 */
    private val fragments: MutableList<Fragment> by lazy { ArrayList() }
    /** 城市的集合。 */
    private val cityList = ArrayList<CityEntity>()
    /** 当前查看城市下标。 */
    private var mCurIndex = 0
    /** 当前的天气code。 */
    var currentCode = ""


    override fun onResume() {
        super.onResume()
        if (ContentUtil.CITY_CHANGE) {//是否改变了城市
            viewModel.getCities()
            ContentUtil.CITY_CHANGE = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.ivEffect.drawable?.let {
            (it as Animatable).stop()
        }
    }

    override fun bindView() = ActivityMainBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {}

    override fun initView() {
        hideTitleBar()//隐藏顶部标题
        transparentStatusBar()//透明状态栏

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

        mBinding.ivBg.setImageResource(IconUtils.defaultBg)
    }

    override fun initEvent() {
        mBinding.ivSetting.setOnClickListener { _ ->
            startActivity<SettingsActivity>()
        }
        mBinding.ivAddCity.setOnClickListener { _ ->
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
     * 显示城市(处理小白点、VP+Fragment)。
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
        val size = DisplayUtil.dp2px(4f)
        val layoutParams = LinearLayout.LayoutParams(size, size)
        // 设置间隔
        layoutParams.rightMargin = 12

        for (i in cityList.indices) {
            // 创建底部指示器(小圆点)
            val view = View(this@HomeActivity)
            view.setBackgroundResource(R.drawable.background)
            view.isEnabled = false

            // 添加到LinearLayout
            mBinding.llRound.addView(view, layoutParams)
        }
        // 小白点
        mBinding.llRound.getChildAt(mCurIndex).isEnabled = true
        mBinding.llRound.visibility = if (cityList.size <= 1) View.GONE else View.VISIBLE

        fragments.clear()
        for (city in cityList) {
            val cityId = city.cityId
//            LogUtil.i("cityId: " + cityId)
            val weatherFragment = WeatherFragment.newInstance(cityId)
            fragments.add(weatherFragment)
        }

//        mBinding.viewPager.adapter?.notifyDataSetChanged()
        mBinding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        mBinding.viewPager.currentItem = mCurIndex
    }

    /**
     * 改变背景。
     * @param condCode 天气的代号。
     */
    private fun changeBg(condCode: String) {
        if (currentCode == condCode) {
            return
        }
        currentCode = condCode
        // 获取背景
        val bgDrawable = IconUtils.getBg(this@HomeActivity, condCode.toInt())
        mBinding.ivBg.setImageResource(bgDrawable)

        // 获取特效
        val effectDrawable = EffectUtil.getEffect(context, condCode.toInt())
        mBinding.ivEffect.setImageDrawable(effectDrawable)
    }

}