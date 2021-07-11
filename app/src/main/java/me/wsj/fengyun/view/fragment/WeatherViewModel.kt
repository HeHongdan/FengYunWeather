package me.wsj.fengyun.view.fragment

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.Now
import me.wsj.fengyun.bean.Warning
import me.wsj.fengyun.bean.WarningBean
import me.wsj.fengyun.bean.WeatherNow
import me.wsj.fengyun.view.base.BaseViewModel
import me.wsj.lib.Constants
import me.wsj.lib.net.HttpUtils

class WeatherViewModel(val app: Application) : BaseViewModel(app) {

    val weatherNow = MutableLiveData<Now>()
    val warning = MutableLiveData<Warning>()

    fun loadData(cityId: String) {
        val param = HashMap<String, Any>()
        param["location"] = cityId
        param["key"] = Constants.APK_KEY

        // 实时天气
        launch {
            val url = "https://devapi.qweather.com/v7/weather/now"
            HttpUtils.get<WeatherNow>(url, param) { code, result ->
                weatherNow.value = result.now
            }
        }

        // 预警
        launch {
            val url = "https://devapi.qweather.com/v7/warning/now"
            HttpUtils.get<WarningBean>(url, param) { code, result ->
                if (result.warning.isNotEmpty()) {
                    warning.value = result.warning[0]
                }
            }
        }
    }


    /**
     * 获取星期
     *
     * @param num 0-6
     * @return 星期
     */
    private fun getWeek(num: Int): String {
        var week = " "
        when (num) {
            1 -> week = "周一"
            2 -> week = "周二"
            3 -> week = "周三"
            4 -> week = "周四"
            5 -> week = "周五"
            6 -> week = "周六"
            7 -> week = "周日"
        }
        return week
    }

}