package me.wsj.fengyun.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import me.wsj.fengyun.adapter.ForecastAdapter
import me.wsj.fengyun.bean.Air
import me.wsj.fengyun.bean.Daily
import me.wsj.fengyun.bean.Now
import me.wsj.fengyun.bean.Warning
import me.wsj.fengyun.databinding.FragmentWeatherBinding
import me.wsj.fengyun.databinding.LayoutTodayDetailBinding
import me.wsj.fengyun.extension.notEmpty
import me.wsj.fengyun.presenters.WeatherInterface
import me.wsj.fengyun.presenters.impl.WeatherImpl
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.IconUtils
import me.wsj.fengyun.utils.WeatherUtil
import me.wsj.fengyun.view.activity.vm.MainViewModel
import me.wsj.fengyun.view.base.BaseVmFragment
import me.wsj.fengyun.widget.horizonview.ScrollWatched
import me.wsj.fengyun.widget.horizonview.ScrollWatcher
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import per.wsj.commonlib.utils.LogUtil
import java.util.*

private const val PARAM_CITY_ID = "param_city_id"


class WeatherFragment : BaseVmFragment<FragmentWeatherBinding, WeatherViewModel>(),
    WeatherInterface {

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
    private var weatherForecastBean: WeatherDailyBean? = null
    private var weatherHourlyBean: WeatherHourlyBean? = null
    private var nowTmp: String? = null

    private var condCode: String? = null

    private var forecastAdapter: ForecastAdapter? = null

    private var todayDetailBinding: LayoutTodayDetailBinding? = null

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

    override fun bindView() = FragmentWeatherBinding.inflate(layoutInflater)

    override fun initView(view: View?) {
        // must use activity
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        getCurrentTime()

        todayDetailBinding = LayoutTodayDetailBinding.bind(mBinding.root)

        mBinding.horizontalScrollView.setToday24HourView(mBinding.hourly)

        //横向滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
                watched.notifyWatcher(scrollX)
            }
        }

        mBinding.swipeLayout.setOnRefreshListener { loadData() }
    }

    override fun initEvent() {
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

        viewModel.weatherNow.observe(this) {
            showWeatherNow(it)
        }

        viewModel.warning.observe(this) {
            showWarning(it)
        }

        viewModel.airNow.observe(this) {
            showAirNow(it)
        }

        viewModel.forecast.observe(this) {
            showForecast(it)
        }
    }

    /**
     *
     */
    private fun showForecast(dailyForecast: List<Daily>) {
        getCurrentTime()
        val forecastBase = dailyForecast[0]
        val condCodeD = forecastBase.iconDay
        val condCodeN = forecastBase.iconNight
        val tmpMin = forecastBase.tempMin
        val tmpMax = forecastBase.tempMax
        sunrise = forecastBase.sunrise
        sunset = forecastBase.sunset
        moonRise = forecastBase.moonrise
        moonSet = forecastBase.moonset
        todayDetailBinding!!.sunView.setTimes(sunrise, sunset, currentTime)
        todayDetailBinding!!.moonView.setTimes(moonRise, moonSet, currentTime)
        todayMaxTmp = tmpMax
        todayMinTmp = tmpMin
        todayDetailBinding!!.tvMaxTmp.text = "$tmpMax°"
        todayDetailBinding!!.tvMinTmp.text = "$tmpMin°"
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
        todayDetailBinding!!.ivLine2.visibility = View.VISIBLE
        todayDetailBinding!!.gridAir.visibility = View.VISIBLE
        todayDetailBinding!!.rvAir.visibility = View.VISIBLE
        todayDetailBinding!!.airTitle.visibility = View.VISIBLE

        todayDetailBinding!!.tvAir.text = airNow.category
        todayDetailBinding!!.tvAirNum.text = airNow.aqi
        todayDetailBinding!!.tvTodayPm25.text = airNow.pm2p5
        todayDetailBinding!!.tvTodayPm10.text = airNow.pm10
        todayDetailBinding!!.tvTodaySo2.text = airNow.so2
        todayDetailBinding!!.tvTodayNo2.text = airNow.no2
        todayDetailBinding!!.tvTodayCo.text = airNow.co
        todayDetailBinding!!.tvTodayO3.text = airNow.o3
        todayDetailBinding!!.rvAir.background =
            WeatherUtil.getAirBackground(requireContext(), airNow.aqi)
    }

    /**
     * 预警
     */
    private fun showWarning(alarmBase: Warning) {
        mBinding.tvTodayAlarm.visibility = View.VISIBLE
        val level: String = alarmBase.level
        mBinding.tvTodayAlarm.text = alarmBase.typeName + level + "预警"
        val warningRes = WeatherUtil.getWarningRes(requireContext(), level)
        mBinding.tvTodayAlarm.background = warningRes.first
        mBinding.tvTodayAlarm.setTextColor(warningRes.second)
    }

    override fun loadData() {
        val weatherImpl = WeatherImpl(this.activity, this)
        weatherImpl.getWeatherHourly(mCityId)

//        weatherImpl.getWeatherForecast(mCityId)

        viewModel.loadData(mCityId)
    }

    override fun onResume() {
        super.onResume()
        condCode?.let {
            mainViewModel.setCondCode(it)
//            LogUtil.e("-----------------ch bg------------------ ch bg$it")
        }

        setViewTime()
    }

    @SuppressLint("SetTextI18n")
    fun showWeatherNow(now: Now) {
        condCode = now.icon
        nowTmp = now.temp

        mainViewModel.setCondCode(now.icon)
        mBinding.tvTodayCond.text = now.text
        mBinding.tvTodayTmp.text = "${now.temp}°C"
        if (ContentUtil.APP_SETTING_UNIT == "hua") {
            mBinding.tvTodayTmp.text = WeatherUtil.getF(now.temp).toString() + "°F"
        }
        todayDetailBinding!!.tvTodayRain.text = now.precip + "mm"
        todayDetailBinding!!.tvTodayPressure.text = now.pressure + "HPA"
        todayDetailBinding!!.tvTodayHum.text = "${now.humidity}%"
        todayDetailBinding!!.tvTodayVisible.text = now.vis + "KM"
        todayDetailBinding!!.tvWindDir.text = now.windDir
        todayDetailBinding!!.tvWindSc.text = now.windScale + "级"
        val nowTime = DateTime.now()
        val hourOfDay = nowTime.hourOfDay
        if (hourOfDay in 7..18) {
            mBinding.ivBack.setImageResource(IconUtils.getDayBack(context, condCode))
        } else {
            mBinding.ivBack.setImageResource(IconUtils.getNightBack(context, condCode))
        }
        mBinding.swipeLayout.isRefreshing = false
    }

    override fun getWeatherHourly(bean: WeatherHourlyBean?) {
        if (bean != null && bean.hourly != null) {
            weatherHourlyBean = bean
            val hourlyWeatherList = bean.hourly
            val data: MutableList<WeatherHourlyBean.HourlyBean> = ArrayList()
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
            todayDetailBinding!!.sunView.setTimes(sunrise, sunset, currentTime)
            todayDetailBinding!!.moonView.setTimes(moonRise, moonSet, currentTime)
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
        if (todayDetailBinding!!.tvMaxTmp != null) {
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
        }
        getWeatherHourly(weatherHourlyBean)
//        getWeatherForecast(weatherForecastBean)
    }
}