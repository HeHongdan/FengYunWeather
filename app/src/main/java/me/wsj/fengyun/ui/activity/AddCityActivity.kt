package me.wsj.fengyun.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import me.wsj.fengyun.adapter.SearchAdapter
import me.wsj.fengyun.adapter.TopCityAdapter
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.databinding.ActivityAddCityBinding
import me.wsj.fengyun.databinding.ActivityAddCityBinding.*
import me.wsj.fengyun.ui.activity.vm.SearchViewModel
import me.wsj.fengyun.ui.base.BaseVmActivity
import me.wsj.fengyun.ui.fragment.PermissionFragment
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.expand
import me.wsj.lib.extension.startActivity
import me.wsj.lib.extension.toast
import me.wsj.lib.net.LoadState
import per.wsj.commonlib.permission.PermissionUtil
import java.util.*

/**
 * 类描述：选择城市。
 *
 * @author HeHongdan
 * @date 2021/8/9
 * @since v2021/8/9
 */
class AddCityActivity : BaseVmActivity<ActivityAddCityBinding, SearchViewModel>() {

    /** 搜索城市适配器。 */
    private var searchAdapter: SearchAdapter? = null
    /** 热门城市适配器。 */
    private var topCityAdapter: TopCityAdapter? = null
    /** 搜索城市的集合。 */
    private val searchCities by lazy { ArrayList<CityBean>() }
    /** 热门城市的集合。 */
    private val topCities by lazy { ArrayList<String>() }
    /** 从闪屏页跳转过来。 */
    private var fromSplash = false
    /** 是否需要请求定位。 */
    private var requestedGPS = false


    /**
     * 绑定视图。
     *
     * @return [@androidx.annotation.NonNull] ActivityAddCityBinding
     */
    override fun bindView() = inflate(layoutInflater)

    /**
     * 准备好数据。
     *
     * @param intent 意图(可空)。
     */
    override fun prepareData(intent: Intent?) {
        intent?.let {
            fromSplash = it.getBooleanExtra("fromSplash", false)//初始化是否 闪屏页 跳转过来
        }
    }

    override fun initView() {
        setTitle("添加城市")
        mBinding.etSearch.threshold = 2//输入2个字开始自动提示

        searchAdapter = SearchAdapter(
            this@AddCityActivity,
            searchCities,
            mBinding.etSearch.text.toString()
        ) {
            viewModel.addCity(it)
        }
        mBinding.rvSearch.adapter = searchAdapter//赋值(搜索城市)RV的适配器

        topCityAdapter = TopCityAdapter(topCities) {
            viewModel.getCityInfo(it)
        }
        val layoutManager = GridLayoutManager(context, 3)//显示3列
        mBinding.rvTopCity.adapter = topCityAdapter//赋值(热门城市)RV的适配器
        mBinding.rvTopCity.layoutManager = layoutManager

        mBinding.tvGetPos.expand(10, 10)//响应区域扩大
        //编辑框输入监听
        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keywords = mBinding.etSearch.text.toString()
                if (!TextUtils.isEmpty(keywords)) {
                    viewModel.searchCity(keywords)//搜索城市
                } else {
                    mBinding.rvSearch.visibility = View.GONE//隐藏搜索结果的RV
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        mBinding.tvGetPos.setOnClickListener {//定位按钮的监听器
            hideKeyboard()
            checkAndOpenGPS()
        }
    }

    override fun initEvent() {
        //缓存定位发生变化
        viewModel.cacheLocation.observe(this) {
            mBinding.tvCurLocation.visibility = View.VISIBLE
            mBinding.tvCurLocation.text = it//it：viewModel.cacheLocation
        }

        // 定位获取的数据
        viewModel.curLocation.observe(this) {
            mBinding.tvCurLocation.visibility = View.VISIBLE
            mBinding.tvCurLocation.text = it
            viewModel.getCityInfo(it, true)
        }

        // 根据定位城市获取详细信息
        viewModel.curCity.observe(this) { item ->
            val curCity = location2CityBean(item)
            // 显示城市详细位置
            mBinding.tvCurLocation.text = curCity.cityName
            viewModel.addCity(curCity, true)
        }

        // 选中的热门城市的信息
        viewModel.choosedCity.observe(this) { item ->
            val curCity = location2CityBean(item)
            viewModel.addCity(curCity)
        }

        viewModel.loadState.observe(this) {
            when (it) {// it：viewModel.loadState
                is LoadState.Start -> {
                    showLoading(true, it.tip)//显示加载中提示框
                }
                is LoadState.Error -> {
                    toast(it.msg)//吐司失败
                }
                is LoadState.Finish -> {
                    if (viewModel.isStopped()) {//是否完成请求
                        showLoading(false)
                    }
                }
            }
        }

        //添加城市是否完成
        viewModel.addFinish.observe(this) {
            if (it) {//添加完成
                if (fromSplash) {//闪屏页跳转过来
                    startActivity<HomeActivity>()//跳转天气详情
                }
                ContentUtil.CITY_CHANGE = true
                finish()
            }
        }

        viewModel.searchResult.observe(this) {
            showSearchResult(it)
        }

        viewModel.topCity.observe(this) {
            showTopCity(it)
        }
    }

    override fun initData() {
        viewModel.getTopCity()//获取热点城市
        viewModel.getCacheLocation()//获取缓存的位置
    }

    /**
     * 检查并打开GPS。
     */
    fun checkAndOpenGPS() {
        if (checkGPSPermission()) {//是否有定位权限
            if (checkGPSOpen()) {//是否打开定位
                viewModel.getLocation()//获取定位信息
            } else {
                requestedGPS = true
                openGPS()//打开定位
            }
        } else {
            toast("没有【定位】权限")
        }
    }

    /**
     * 检查GPS权限。
     *
     * @return 是否有定位权限。
     */
    fun checkGPSPermission(): Boolean {
        val pm1 = PermissionUtil.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val pm2 = PermissionUtil.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return (pm1 || pm2)
    }

    /**
     * 检查GPS状态。
     *
     * @return 是否打开GPS。
     */
    fun checkGPSOpen(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val pr1 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val pr2 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return (pr1 || pr2)
    }

    /**
     * 打开GPS。
     */
    private fun openGPS() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(PermissionFragment.newInstance(), "permission_fragment")
        beginTransaction.commitAllowingStateLoss()
    }

    /**
     * 展示热门城市到 RV。
     *
     * @param locations 热门城市集合。
     */
    private fun showTopCity(locations: List<String>) {
        topCities.clear()
//        locations.forEach { item ->
//            var parentCity = item.adm2
//            val adminArea = item.adm1
//            val city = item.country
//            if (TextUtils.isEmpty(parentCity)) {
//                parentCity = adminArea
//            }
//            if (TextUtils.isEmpty(adminArea)) {
//                parentCity = city
//            }
//            val cityBean = CityBean()
//            cityBean.cityName = parentCity + " - " + item.name
//            cityBean.cityId = item.id
//            cityBean.cnty = city
//            cityBean.adminArea = adminArea
//            topCities.add(cityBean)
//        }
        topCities.addAll(locations)
        topCityAdapter?.notifyDataSetChanged()
    }

    /**
     * 展示搜索(城市)结果到 RV。
     *
     * @param basic 城市(定位)集合。
     */
    private fun showSearchResult(basic: List<Location>) {
        mBinding.rvSearch.visibility = View.VISIBLE

        searchCities.clear()

        basic.forEach { item ->
            searchCities.add(location2CityBean(item))
        }
        searchAdapter?.notifyDataSetChanged()
    }

    /**
     * 定位 转 城市对象。
     *
     * @param location 定位。
     * @return 城市。
     */
    private fun location2CityBean(location: Location): CityBean {
        var parentCity = location.adm2
        val adminArea = location.adm1
        val city = location.country
        if (TextUtils.isEmpty(parentCity)) {
            parentCity = adminArea
        }
        if (TextUtils.isEmpty(adminArea)) {
            parentCity = city
        }
        val cityBean = CityBean()
        cityBean.cityName = parentCity + " - " + location.name
        cityBean.cityId = location.id
        cityBean.cnty = city
        cityBean.adminArea = adminArea
        return cityBean
    }

    override fun onResume() {
        super.onResume()
        if (requestedGPS) {//是否需要请求定位
            requestedGPS = false
            if (checkGPSOpen()) {//是否打开GPS
                viewModel.getLocation()//获取得信息
            } else {
                toast("无法获取位置信息")
            }
        }
    }

    override fun finish() {
        super.finish()
        hideKeyboard()
    }

    /**
     * 隐藏输入法。
     */
    private fun hideKeyboard() {
        currentFocus?.let {//当前activity获得焦点
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager//输入法管理器
            imm.hideSoftInputFromWindow(it.windowToken, 0)//隐藏输入法
        }
    }

    companion object {
        fun startActivity(context: Context, fromSplash: Boolean = false) {
            val intent = Intent(context, AddCityActivity::class.java)
            intent.putExtra("fromSplash", fromSplash)
            context.startActivity(intent)
        }
    }
}