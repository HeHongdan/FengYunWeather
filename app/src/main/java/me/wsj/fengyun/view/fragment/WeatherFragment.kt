package me.wsj.fengyun.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.qweather.sdk.bean.WarningBean
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.ForecastAdapter
import me.wsj.fengyun.databinding.FragmentWeatherBinding
import me.wsj.fengyun.databinding.LayoutTodayDetailBinding
import me.wsj.fengyun.extension.notEmpty
import me.wsj.fengyun.presenters.WeatherInterface
import me.wsj.fengyun.presenters.impl.WeatherImpl
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.IconUtils
import me.wsj.fengyun.utils.TransUnitUtil
import me.wsj.fengyun.view.base.BaseFragment
import me.wsj.fengyun.widget.horizonview.ScrollWatched
import me.wsj.fengyun.widget.horizonview.ScrollWatcher
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import per.wsj.commonlib.utils.LogUtil
import java.util.*

private const val PARAM_CITY_ID = "param_city_id"


class WeatherFragment : BaseFragment<FragmentWeatherBinding>(), WeatherInterface {

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

    private lateinit var viewModel: WeatherViewModel

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
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        todayDetailBinding = LayoutTodayDetailBinding.bind(mBinding.root)

        getCurrentTime()

        mBinding.horizontalScrollView.setToday24HourView(mBinding.hourly)

        //横向滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.horizontalScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                watched.notifyWatcher(scrollX)
            }
        }

        mBinding.swipeLayout.setOnRefreshListener { initData(mCityId) }
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
    }

    override fun loadData() {
        initData(mCityId)


        setViewTime()

        getWeatherForecast(weatherForecastBean)
    }

    private fun initData(cityId: String) {
        val weatherImpl = WeatherImpl(this.activity, this)
        weatherImpl.getWeatherHourly(cityId)
        weatherImpl.getAirForecast(cityId)
        weatherImpl.getAirNow(cityId)
        weatherImpl.getWarning(cityId)
        weatherImpl.getWeatherForecast(cityId)
        weatherImpl.getWeatherNow(cityId)
    }

    override fun onResume() {
        super.onResume()
        condCode?.let {
            viewModel.setCondCode(it)
            LogUtil.e("-----------------ch bg------------------ ch bg$it")
        }

        setViewTime()
    }

    @SuppressLint("SetTextI18n")
    override fun getWeatherNow(bean: WeatherNowBean?) {
        if (bean != null && bean.now != null) {
            val now = bean.now
            val rain = now.precip
            val hum = now.humidity
            val pres = now.pressure
            val vis = now.vis
            val windDir = now.windDir
            val windSc = now.windScale
            val condTxt = now.text
            condCode = now.icon
            viewModel.setCondCode(condCode!!)
            nowTmp = now.temp
            mBinding.tvTodayCond.text = condTxt
            mBinding.tvTodayTmp.text = "$nowTmp°C"
            if (ContentUtil.APP_SETTING_UNIT == "hua") {
                mBinding.tvTodayTmp.text = TransUnitUtil.getF(nowTmp).toString() + "°F"
            }
            todayDetailBinding!!.tvTodayRain.text = rain + "mm"
            todayDetailBinding!!.tvTodayPressure.text = pres + "HPA"
            todayDetailBinding!!.tvTodayHum.text = "$hum%"
            todayDetailBinding!!.tvTodayVisible.text = vis + "KM"
            todayDetailBinding!!.tvWindDir.text = windDir
            todayDetailBinding!!.tvWindSc.text = windSc + "级"
            val nowTime = DateTime.now()
            val hourOfDay = nowTime.hourOfDay
            if (hourOfDay in 7..18) {
                mBinding.ivBack.setImageResource(IconUtils.getDayBack(context, condCode))
            } else {
                mBinding.ivBack.setImageResource(IconUtils.getNightBack(context, condCode))
            }
            mBinding.swipeLayout.isRefreshing = false
        }
    }

    override fun getWeatherForecast(bean: WeatherDailyBean?) {
        if (bean != null && bean.daily != null) {
            weatherForecastBean = bean
            getCurrentTime()
            val dailyForecast = bean.daily
            val forecastBase = dailyForecast[0]
            val condCodeD = forecastBase.iconDay
            val condCodeN = forecastBase.iconNight
            val tmpMin = forecastBase.tempMin
            val tmpMax = forecastBase.tempMax
            sunrise = forecastBase.sunrise
            sunset = forecastBase.sunset
            moonRise = forecastBase.moonRise
            moonSet = forecastBase.moonSet
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
    }

    override fun getWarning(alarmBase: WarningBean.WarningBeanBase?) {
        if (alarmBase != null) {
            mBinding.tvTodayAlarm.visibility = View.VISIBLE
            val level: String = alarmBase.level
            val type: String = alarmBase.type
            mBinding.tvTodayAlarm.text = type + "预警"
            if (!TextUtils.isEmpty(level)) {
                when (level) {
                    "蓝色", "Blue" -> {
                        mBinding.tvTodayAlarm.background =
                            resources.getDrawable(R.drawable.shape_blue_alarm)
                        mBinding.tvTodayAlarm.setTextColor(resources.getColor(R.color.white))
                    }
                    "黄色", "Yellow" -> {
                        mBinding.tvTodayAlarm.background =
                            resources.getDrawable(R.drawable.shape_yellow_alarm)
                        mBinding.tvTodayAlarm.setTextColor(resources.getColor(R.color.white))
                    }
                    "橙色", "Orange" -> {
                        mBinding.tvTodayAlarm.background =
                            resources.getDrawable(R.drawable.shape_orange_alarm)
                        mBinding.tvTodayAlarm.setTextColor(resources.getColor(R.color.white))
                    }
                    "红色", "Red" -> {
                        mBinding.tvTodayAlarm.background =
                            resources.getDrawable(R.drawable.shape_red_alarm)
                        mBinding.tvTodayAlarm.setTextColor(resources.getColor(R.color.white))
                    }
                    "白色", "White" -> {
                        mBinding.tvTodayAlarm.background =
                            resources.getDrawable(R.drawable.shape_white_alarm)
                        mBinding.tvTodayAlarm.setTextColor(resources.getColor(R.color.black))
                    }
                }
            }
        } else {
            mBinding.tvTodayAlarm.visibility = View.GONE
        }
    }

    override fun getAirNow(bean: AirNowBean?) {
        if (bean != null && bean.now != null) {
            todayDetailBinding!!.ivLine2.visibility = View.VISIBLE
            todayDetailBinding!!.gridAir.visibility = View.VISIBLE
            todayDetailBinding!!.rvAir.visibility = View.VISIBLE
            todayDetailBinding!!.airTitle.visibility = View.VISIBLE
            val airNowCity = bean.now
            val qlty = airNowCity.category
            val aqi = airNowCity.aqi
            val pm25 = airNowCity.pm2p5
            val pm10 = airNowCity.pm10
            val so2 = airNowCity.so2
            val no2 = airNowCity.no2
            val co = airNowCity.co
            val o3 = airNowCity.o3
            todayDetailBinding!!.tvAir.text = qlty
            todayDetailBinding!!.tvAirNum.text = aqi
            todayDetailBinding!!.tvTodayPm25.text = pm25
            todayDetailBinding!!.tvTodayPm10.text = pm10
            todayDetailBinding!!.tvTodaySo2.text = so2
            todayDetailBinding!!.tvTodayNo2.text = no2
            todayDetailBinding!!.tvTodayCo.text = co
            todayDetailBinding!!.tvTodayO3.text = o3
            todayDetailBinding!!.rvAir.background = viewModel.getAirBackground(aqi)
        } else {
            todayDetailBinding!!.ivLine2.visibility = View.GONE
            todayDetailBinding!!.gridAir.visibility = View.GONE
            todayDetailBinding!!.rvAir.visibility = View.GONE
            todayDetailBinding!!.airTitle.visibility = View.GONE
        }
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
                mBinding.tvLineMaxTmp.text = TransUnitUtil.getF(maxTmp.toString()).toString() + "°"
                mBinding.tvLineMinTmp.text = TransUnitUtil.getF(minTmp.toString()).toString() + "°"
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
                    TransUnitUtil.getF(todayMaxTmp).toString() + "°F"
                todayDetailBinding!!.tvMinTmp.text =
                    TransUnitUtil.getF(todayMinTmp).toString() + "°F"
                mBinding.tvTodayTmp.text = TransUnitUtil.getF(nowTmp).toString() + "°F"
            } else {
                LogUtil.e("当前城市2：$condCode")
                todayDetailBinding!!.tvMaxTmp.text = "$todayMaxTmp°C"
                todayDetailBinding!!.tvMinTmp.text = "$todayMinTmp°C"
                mBinding.tvTodayTmp.text = "$nowTmp°C"
            }
        }
        getWeatherHourly(weatherHourlyBean)
        getWeatherForecast(weatherForecastBean)
    }
}