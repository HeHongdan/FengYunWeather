package me.wsj.fengyun.view.activity

import android.Manifest
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.qweather.sdk.bean.geo.GeoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ViewPagerAdapter
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.CityBeanList
import me.wsj.fengyun.dataInterface.DataInterface
import me.wsj.fengyun.dataInterface.DataUtil
import me.wsj.fengyun.databinding.ActivityMainBinding
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.IconUtils
import me.wsj.fengyun.utils.SpUtils
import me.wsj.fengyun.view.activity.vm.MainViewModel
import me.wsj.fengyun.view.base.BaseActivity
import me.wsj.fengyun.view.fragment.WeatherFragment
import me.wsj.fengyun.view.window.LocListWindow
import org.joda.time.DateTime
import per.wsj.commonlib.permission.PermissionUtil
import per.wsj.commonlib.utils.DisplayUtil
import per.wsj.commonlib.utils.LogUtil
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(), DataInterface {

    private val fragments: MutableList<Fragment> by lazy { ArrayList() }

    private val mCityList: MutableList<String> by lazy { ArrayList() }

    private val cityIds: MutableList<String> by lazy { ArrayList() }

    private var mCurIndex = 0

    var cityBeanList: CityBeanList? = CityBeanList()

    private var condCode: String? = null

    private var isFirst = true

    private val viewModel: MainViewModel by viewModels()

    override fun bindView() = ActivityMainBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent) {

    }

    override fun initView() {
        hideTitleBar()
        //透明状态栏
        transparentStatusBar()
        initCities(true)

        lifecycleScope.launch(Dispatchers.IO) {
            AppRepo.getInstance().saveCache("wsj", "123456")
            val data = AppRepo.getInstance().getCache<String>("wsj")
        }
    }

    private fun initCities(first: Boolean) {
        cityBeanList = SpUtils.getBean(this@MainActivity, "cityBean", CityBeanList::class.java)
        val cityBean = SpUtils.getBean(this@MainActivity, "cityBean", CityBeanList::class.java)

        if (cityBean != null && cityBean.cityBeans.size > 0) {
            for (i in cityBean.cityBeans.indices) {
                mCityList.add(cityBean.cityBeans[i].cityName)
            }
        }

        if (first) {
            viewModel.initLocation()
        } else {
            isFirst = false
            viewModel.getCityByCoordinate()
        }
    }

    override fun initEvent() {
        mBinding.ivSetting.setOnClickListener { v ->
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
        mBinding.ivAddCity.setOnClickListener { v ->
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }

        viewModel.curCid.observe(this, { cid ->
            if (cityIds.isNotEmpty()) {
                cityIds.add(0, cid)
                cityIds.removeAt(1)
            }
            viewModel.getWeatherByCid(cid)
        })

        viewModel.condCode.observe(this, { code ->
            condCode = code
            val nowTime = DateTime.now()
            val hourOfDay = nowTime.hourOfDay
            if (hourOfDay in 7..18) {
                mBinding.ivBg.setImageResource(IconUtils.getDayBack(this@MainActivity, condCode))
            } else {
                mBinding.ivBg.setImageResource(IconUtils.getNightBack(this@MainActivity, condCode))
            }
        })

        viewModel.locationBean.observe(this, { locationBean: GeoBean.LocationBean ->
            val cid = locationBean.id
            val cityName = locationBean.name
            if (isFirst) {
                ContentUtil.NOW_CITY_ID = cid
                ContentUtil.NOW_CITY_NAME = cityName
            }
            var cityBeans: MutableList<CityBean> = ArrayList()
            val cityBean = CityBean()
            cityBean.cityName = cityName
            cityBean.cityId = cid
            mCityList.add(0, cityName)

            if (cityBeanList != null && cityBeanList!!.cityBeans != null && cityBeanList!!.cityBeans.size > 0) {
                cityBeans = cityBeanList!!.cityBeans
                cityBeans.add(0, cityBean)
            } else {
                cityBeans.add(cityBean)
            }
            mBinding.tvLocation.text = cityName
            showCity(cityBeans, isFirst)
        })

        viewModel.cityBean.observe(this, { cityBeans -> showCity(cityBeans, isFirst) })

        viewModel.obtainLocation.observe(this, { isObtain ->
            if (!isObtain) {
                if (!PermissionUtil.hasPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    // 没有权限
                    val view: View =
                        LayoutInflater.from(this@MainActivity).inflate(R.layout.pop_loc_list, null)
                    val locListWindow = LocListWindow(
                        view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        this@MainActivity
                    )
                    locListWindow.show()
                    locListWindow.showAtLocation(mBinding.tvLocation, Gravity.CENTER, 0, 0)
                    if (ContentUtil.FIRST_OPEN) {
                        ContentUtil.FIRST_OPEN = false
                        SpUtils.putBoolean(this@MainActivity, "first_open", false)
                    }
                }
            }
            isFirst = true
            viewModel.getCityByCoordinate()
        })
    }

    override fun initData() {}

    /**
     * 显示城市
     */
    private fun showCity(cityBeans: List<CityBean>, first: Boolean) {
        mBinding.llRound.removeAllViews()
        fragments.clear()
        for (city in cityBeans) {
            val cityId = city.cityId
            cityIds.add(cityId)
            val weatherFragment = WeatherFragment.newInstance(cityId)
            fragments.add(weatherFragment)
        }
        if (cityIds[0].equals(ContentUtil.NOW_CITY_ID, ignoreCase = true)) {
            mBinding.ivLoc.visibility = View.VISIBLE
        } else {
            mBinding.ivLoc.visibility = View.INVISIBLE
        }

        // 宽高参数
        val layoutParams = LinearLayout.LayoutParams(
            DisplayUtil.dip2px(this, 4f),
            DisplayUtil.dip2px(this, 4f)
        )
        //设置间隔
        layoutParams.rightMargin = 12

        for (i in fragments.indices) {
            //创建底部指示器(小圆点)
            val view = View(this@MainActivity)
            view.setBackgroundResource(R.drawable.background)
            view.isEnabled = false

            //添加到LinearLayout
            mBinding.llRound.addView(view, layoutParams)
        }
        mBinding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        //第一次显示小白点
        mBinding.llRound.getChildAt(0).isEnabled = true
        mCurIndex = 0
        mBinding.llRound.visibility = if (fragments.size == 1) View.GONE else View.VISIBLE
        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                if (cityIds[i].equals(ContentUtil.NOW_CITY_ID, ignoreCase = true)) {
                    mBinding.ivLoc.visibility = View.VISIBLE
                } else {
                    mBinding.ivLoc.visibility = View.INVISIBLE
                }
                mBinding.llRound.getChildAt(mCurIndex).isEnabled = false
                mBinding.llRound.getChildAt(i).isEnabled = true
                mCurIndex = i
                mBinding.tvLocation.text = mCityList[i]
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
        if (!first && fragments.size > 1) {
            mBinding.viewPager.currentItem = 1
            // 根据城市id获取信息
            LogUtil.LOGE("what the fuck it will run? cityId: " + cityIds[1])
            viewModel.getNow(cityIds[1], false)
        } else {
            mBinding.viewPager.currentItem = 0
            viewModel.getNow(ContentUtil.NOW_LON.toString() + "," + ContentUtil.NOW_LAT, true)
        }
    }

    override fun onResume() {
        super.onResume()
        DataUtil.setDataInterface(this)

        if (ContentUtil.CITY_CHANGE) {
            initCities(true)
            ContentUtil.CITY_CHANGE = false
        }
        if (ContentUtil.UNIT_CHANGE) {
            for (fragment in fragments) {
                val weatherFragment = fragment as WeatherFragment
                weatherFragment.changeUnit()
            }
            ContentUtil.UNIT_CHANGE = false
        }
    }

    override fun setCid(cid: String) {
        initCities(false)
    }

    override fun deleteID(index: Int) {
        initCities(true)
    }

    override fun changeBg(condCode: String) {
        val nowTime = DateTime.now()
        val hourOfDay = nowTime.hourOfDay
        if (hourOfDay in 7..18) {
            mBinding.ivBg.setImageResource(IconUtils.getDayBack(this@MainActivity, condCode))
        } else {
            mBinding.ivBg.setImageResource(IconUtils.getNightBack(this@MainActivity, condCode))
        }
    }
}
