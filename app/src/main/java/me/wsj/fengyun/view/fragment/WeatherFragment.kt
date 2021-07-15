package me.wsj.fengyun.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ForecastAdapter
import me.wsj.fengyun.bean.*
import me.wsj.fengyun.databinding.FragmentWeatherBinding
import me.wsj.fengyun.databinding.LayoutAirQualityBinding
import me.wsj.fengyun.databinding.LayoutSunMoonBinding
import me.wsj.fengyun.databinding.LayoutTodayDetailBinding
import me.wsj.lib.extension.notEmpty
import me.wsj.lib.extension.toastCenter
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.lib.utils.IconUtils
import me.wsj.fengyun.utils.Lunar
import me.wsj.fengyun.utils.WeatherUtil
import me.wsj.fengyun.view.activity.vm.MainViewModel
import me.wsj.fengyun.view.base.BaseVmFragment
import me.wsj.fengyun.view.base.LoadState
import me.wsj.fengyun.widget.horizonview.ScrollWatched
import me.wsj.fengyun.widget.horizonview.ScrollWatcher
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import per.wsj.commonlib.utils.LogUtil
import per.wsj.commonlib.utils.Typefaces
import java.util.*

private const val PARAM_CITY_ID = "param_city_id"


class WeatherFragment : BaseVmFragment<FragmentWeatherBinding, WeatherViewModel>() {

    private lateinit var mCityId: String

    private val watcherList by lazy { ArrayList<ScrollWatcher>() }

    private lateinit var watched: ScrollWatched

    private var currentTime: String? = null
    private var sunrise: String? = null
    private var sunset: String? = null
    private var moonRise: String? = null
    private var moonSet: String? = null
    private var hasAni = false

    private var todayMaxTmp: String? = null
    private var todayMinTmp: String? = null

    private var nowTmp: String? = null

    private var condCode: String? = null

    private var forecastAdapter: ForecastAdapter? = null

    private var todayDetailBinding: LayoutTodayDetailBinding? = null

    private lateinit var airQualityBinding: LayoutAirQualityBinding

    private lateinit var sunMoonBinding: LayoutSunMoonBinding

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCityId = it.getString(PARAM_CITY_ID).toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_CITY_ID, param1)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadCache(mCityId)

        Calendar.getInstance().apply {
            val day = get(Calendar.DAY_OF_MONTH)
            mBinding.tvDate.text =
                (get(Calendar.MONTH) + 1).toString() + "月" + day + "日 农历" +
                        Lunar(this).toString()
        }
    }

    override fun onResume() {
        super.onResume()
        condCode?.let {
            LogUtil.e("onResume() set cond code: $condCode")
            mainViewModel.setCondCode(it)
        }

        setViewTime()
    }

    override fun bindView() = FragmentWeatherBinding.inflate(layoutInflater)

    override fun initView(view: View?) {
        // must use activity
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        todayDetailBinding = LayoutTodayDetailBinding.bind(mBinding.root)

        sunMoonBinding = LayoutSunMoonBinding.bind(mBinding.root)

        airQualityBinding = LayoutAirQualityBinding.bind(mBinding.root)

        mBinding.horizontalScrollView.setToday24HourView(mBinding.hourly)

        watched = object : ScrollWatched {
            override fun addWatcher(watcher: ScrollWatcher) {
                watcherList.add(watcher)
            }

            override fun removeWatcher(watcher: ScrollWatcher) {
                watcherList.remove(watcher)
            }

            override fun notifyWatcher(x: Int) {
                for (watcher in watcherList) {
                    watcher.update(x)
                }
            }
        }

        watched.addWatcher(mBinding.hourly)

        //横向滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                watched.notifyWatcher(scrollX)
            }
        }

        // 设置字体
        mBinding.tvTodayTmp.typeface = Typefaces.get(requireContext(), "widget_clock.ttf")
    }

    override fun initEvent() {
        mBinding.swipeLayout.setOnRefreshListener { loadData() }

        viewModel.weatherNow.observe(this) {
            showWeatherNow(it)
        }

        viewModel.warnings.observe(this) {
            showWarnings(it)
        }

        viewModel.airNow.observe(this) {
            showAirNow(it)
        }

        viewModel.forecast.observe(this) {
            showForecast(it)
        }
        viewModel.hourly.observe(this) {
            showHourly(it)
        }

        viewModel.loadState.observe(this) {
            when (it) {
                is LoadState.Start -> {
                    mBinding.swipeLayout.isRefreshing = true
                }
                is LoadState.Error -> {

                }
                is LoadState.Finish -> {
                    if (viewModel.isStopped()) {
                        mBinding.swipeLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    override fun loadData() {
        viewModel.loadData(mCityId)
    }

    @SuppressLint("SetTextI18n")
    fun showWeatherNow(now: Now) {
        condCode = now.icon
        nowTmp = now.temp
        lifecycleScope.launchWhenResumed {
            LogUtil.e("showWeatherNow() set cond code: $condCode")
            mainViewModel.setCondCode(now.icon)
        }
        mBinding.tvTodayCond.text = now.text
        mBinding.tvTodayTmp.text = now.temp
        mBinding.tvUnit.visibility = View.VISIBLE

        if (ContentUtil.APP_SETTING_UNIT == "hua") {
            mBinding.tvTodayTmp.text = WeatherUtil.getF(now.temp).toString() + "°F"
        }
        todayDetailBinding!!.tvTodayRain.text = now.precip + "mm"
        todayDetailBinding!!.tvTodayPressure.text = now.pressure + "HPA"
        todayDetailBinding!!.tvTodayHum.text = "${now.humidity}%"
        todayDetailBinding!!.tvTodayVisible.text = now.vis + "KM"
        todayDetailBinding!!.tvWindDir.text = now.windDir
        todayDetailBinding!!.tvWindSc.text = now.windScale + "级"
    }

    /**
     * 三天预报
     */
    private fun showForecast(dailyForecast: List<Daily>) {
        getCurrentTime()
        val forecastBase = dailyForecast[0]
        val condCodeD = forecastBase.iconDay
        val condCodeN = forecastBase.iconNight
        todayMinTmp = forecastBase.tempMin
        todayMaxTmp = forecastBase.tempMax
        sunrise = forecastBase.sunrise
        sunset = forecastBase.sunset
        moonRise = forecastBase.moonrise
        moonSet = forecastBase.moonset
        sunMoonBinding.sunView.setTimes(sunrise, sunset, currentTime)
        sunMoonBinding.moonView.setTimes(moonRise, moonSet, currentTime)
        todayDetailBinding!!.tvMaxTmp.text = "$todayMaxTmp°"
        todayDetailBinding!!.tvMinTmp.text = "$todayMinTmp°"
        todayDetailBinding!!.ivTodayDay.setImageResource(
            IconUtils.getDayIconDark(
                context,
                condCodeD
            )
        )
        todayDetailBinding!!.ivTodayNight.setImageResource(
            IconUtils.getNightIconDark(
                context,
                condCodeN
            )
        )
        if (forecastAdapter == null) {
            forecastAdapter = ForecastAdapter(activity, dailyForecast)
            mBinding.rvForecast.adapter = forecastAdapter
            val forecastManager = LinearLayoutManager(
                activity
            )
            forecastManager.orientation = LinearLayoutManager.VERTICAL
            mBinding.rvForecast.layoutManager = forecastManager
        } else {
            forecastAdapter!!.refreshData(activity, dailyForecast)
        }
    }

    /**
     * 空气质量
     */
    private fun showAirNow(airNow: Air) {
        airQualityBinding.gridAir.visibility = View.VISIBLE
        airQualityBinding.rvAir.visibility = View.VISIBLE
        airQualityBinding.airTitle.visibility = View.VISIBLE

        airQualityBinding.tvAir.text = airNow.category
        airQualityBinding.tvAirNum.text = airNow.aqi
        airQualityBinding.tvTodayPm25.text = airNow.pm2p5
        airQualityBinding.tvTodayPm10.text = airNow.pm10
        airQualityBinding.tvTodaySo2.text = airNow.so2
        airQualityBinding.tvTodayNo2.text = airNow.no2
        airQualityBinding.tvTodayCo.text = airNow.co
        airQualityBinding.tvTodayO3.text = airNow.o3
        airQualityBinding.rvAir.background =
            WeatherUtil.getAirBackground(requireContext(), airNow.aqi)
    }

    /**
     * 预警
     */
    private fun showWarnings(warnings: List<Warning>) {
        mBinding.alarmFlipper.visibility = View.VISIBLE
        mBinding.alarmFlipper.setInAnimation(requireContext(), R.anim.bottom_in)
        mBinding.alarmFlipper.setOutAnimation(requireContext(), R.anim.top_out)
        mBinding.alarmFlipper.flipInterval = 3000
        for (warning in warnings) {
            val level: String = warning.level
            val tip = warning.typeName + level + "预警"
            val warningRes = WeatherUtil.getWarningRes(requireContext(), level)
            val textView: TextView = layoutInflater.inflate(R.layout.item_warning, null) as TextView
            textView.background = warningRes.first
            textView.text = tip
            textView.setOnClickListener {
                toastCenter(warning.text)
            }
            textView.setTextColor(warningRes.second)
            mBinding.alarmFlipper.addView(textView)
        }
        if (warnings.size > 1) {
            mBinding.alarmFlipper.startFlipping()
        }
    }

    /**
     * 逐小时天气
     */
    private fun showHourly(hourlyWeatherList: List<Hourly>) {
        val data: MutableList<Hourly> = ArrayList()
        if (hourlyWeatherList.size > 23) {
            for (i in 0..23) {
                data.add(hourlyWeatherList[i])
                val condCode = data[i].icon
                var time = data[i].fxTime
                time = time.substring(time.length - 11, time.length - 9)
                val hourNow = time.toInt()
                if (hourNow in 6..19) {
                    data[i].icon = condCode + "d"
                } else {
                    data[i].icon = condCode + "n"
                }
            }
        } else {
            for (i in hourlyWeatherList.indices) {
                data.add(hourlyWeatherList[i])
                val condCode = data[i].icon
                var time = data[i].fxTime
                time = time.substring(time.length - 11, time.length - 9)
                val hourNow = time.toInt()
                if (hourNow in 6..19) {
                    data[i].icon = condCode + "d"
                } else {
                    data[i].icon = condCode + "n"
                }
            }
        }
        var minTmp = data[0].temp.toInt()
        var maxTmp = minTmp
        for (i in data.indices) {
            val tmp = data[i].temp.toInt()
            minTmp = Math.min(tmp, minTmp)
            maxTmp = Math.max(tmp, maxTmp)
        }
        //设置当天的最高最低温度
        mBinding.hourly.setHighestTemp(maxTmp)
        mBinding.hourly.setLowestTemp(minTmp)
        if (maxTmp == minTmp) {
            mBinding.hourly.setLowestTemp(minTmp - 1)
        }
        mBinding.hourly.initData(data)
        mBinding.tvLineMaxTmp.text = "$maxTmp°"
        mBinding.tvLineMinTmp.text = "$minTmp°"
        if (ContentUtil.APP_SETTING_UNIT == "hua") {
            mBinding.tvLineMaxTmp.text = WeatherUtil.getF(maxTmp.toString()).toString() + "°"
            mBinding.tvLineMinTmp.text = WeatherUtil.getF(minTmp.toString()).toString() + "°"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mBinding.root.parent != null) {
            (mBinding.root.parent as ViewGroup).removeView(mBinding.root)
        }
    }

    /**
     * 设置view的时间
     */
    private fun setViewTime() {
        if (!hasAni && sunrise.notEmpty() && sunset.notEmpty() && moonRise.notEmpty() && moonSet.notEmpty()) {
            getCurrentTime()
            sunMoonBinding.sunView.setTimes(sunrise, sunset, currentTime)
            sunMoonBinding.moonView.setTimes(moonRise, moonSet, currentTime)
            hasAni = true
        }
    }

    private fun getCurrentTime() {
        var now = DateTime.now(DateTimeZone.UTC)
        val a = "+8.0".toFloat()
        val minute = a * 60
        now = now.plusMinutes(minute.toInt())
        currentTime = now.toString("HH:mm")
    }

    fun changeUnit() {
        if (ContentUtil.APP_SETTING_UNIT == "hua") {
            LogUtil.e("当前城市1：$condCode")
            todayDetailBinding!!.tvMaxTmp.text =
                WeatherUtil.getF(todayMaxTmp!!).toString() + "°F"
            todayDetailBinding!!.tvMinTmp.text =
                WeatherUtil.getF(todayMinTmp!!).toString() + "°F"
            mBinding.tvTodayTmp.text = WeatherUtil.getF(nowTmp!!).toString() + "°F"
        } else {
            LogUtil.e("当前城市2：$condCode")
            todayDetailBinding!!.tvMaxTmp.text = "$todayMaxTmp°C"
            todayDetailBinding!!.tvMinTmp.text = "$todayMinTmp°C"
            mBinding.tvTodayTmp.text = "$nowTmp°C"
        }
//        getWeatherHourly(weatherHourlyBean)
//        getWeatherForecast(weatherForecastBean)
    }
}