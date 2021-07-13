package me.wsj.fengyun.view.fragment

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.*
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.view.base.BaseViewModel
import me.wsj.lib.Constants
import me.wsj.lib.net.HttpUtils
import per.wsj.commonlib.utils.LogUtil

const val CACHE_WEATHER_NOW = "now_"

class WeatherViewModel(val app: Application) : BaseViewModel(app) {

    val weatherNow = MutableLiveData<Now>()
    val warnings = MutableLiveData<List<Warning>>()
    val airNow = MutableLiveData<Air>()
    val forecast = MutableLiveData<List<Daily>>()
    val hourly = MutableLiveData<List<Hourly>>()

    fun loadCache(cityId: String) {
        launchSilent {
            var cache = AppRepo.getInstance().getCache<Now>(CACHE_WEATHER_NOW + cityId)
            cache?.let {
//                LogUtil.e("now: $it")
                weatherNow.postValue(it)
            }
        }
    }

    fun loadData(cityId: String) {
        val param = HashMap<String, Any>()
        param["location"] = cityId
        param["key"] = Constants.APK_KEY

        // 实时天气
        launch {
            val url = "https://devapi.qweather.com/v7/weather/now"
            HttpUtils.get<WeatherNow>(url, param) { code, result ->
                weatherNow.value = result.now
                launch {
                    AppRepo.getInstance().saveCache(CACHE_WEATHER_NOW + cityId, result.now)
                }
            }
        }

        // 预警
        launch {
            val url = "https://devapi.qweather.com/v7/warning/now"
            HttpUtils.get<WarningBean>(url, param) { code, result ->
                if (result.warning.isNotEmpty()) {
                    warnings.value = result.warning
                }
            }
        }

        // 实时空气
        launch {
            val url = "https://devapi.qweather.com/v7/air/now"
            HttpUtils.get<AirNow>(url, param) { code, result ->
                airNow.value = result.now
            }
        }

        // 3天 天气预报
        launch {
            val url = "https://devapi.qweather.com/v7/weather/3d"
            HttpUtils.get<ForestBean>(url, param) { code, result ->
                forecast.value = result.daily
            }
        }

        // 逐小时天气预报
        launch {
            val url = "https://devapi.qweather.com/v7/weather/24h"
            HttpUtils.get<WeatherHourly>(url, param) { code, result ->
                hourly.value = result.hourly
            }
        }

    }

}