package me.wsj.fengyun.view.fragment

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.qweather.sdk.bean.weather.WeatherNowBean
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.Now
import me.wsj.fengyun.bean.WeatherNow
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.view.base.BaseViewModel
import me.wsj.lib.Constants
import me.wsj.lib.net.HttpUtils
import per.wsj.commonlib.utils.LogUtil

class WeatherViewModel(val app: Application) : BaseViewModel(app) {

    /******************************HomeActivity******************************/

    val mCities = MutableLiveData<List<CityEntity>>()

    val mCurCondCode = MutableLiveData<String>()

    fun setCondCode(condCode: String) {
        mCurCondCode.postValue(condCode)
    }

    fun getCities() {
        launchSilent {
            val cities = AppRepo.getInstance().getCities()
            mCities.postValue(cities)
        }
    }

    /******************************HomeActivity******************************/


    fun getAirBackground(aqi: String): Drawable? {
        val num = aqi.toInt()
        return when {
            num <= 50 -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_excellent, null)
            }
            num <= 100 -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_good, null)
            }
            num <= 150 -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_low, null)
            }
            num <= 200 -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_mid, null)
            }
            num <= 300 -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_bad, null)
            }
            else -> {
                ResourcesCompat.getDrawable(app.resources, R.drawable.shape_aqi_serious, null)
            }
        }
    }

    val weatherNow = MutableLiveData<Now>()

    fun loadData(cityId: String) {
        launch {
            val url = "https://devapi.qweather.com/v7/weather/now"
            val param = HashMap<String, Any>()
            param["location"] = cityId
            param["key"] = Constants.APK_KEY
            HttpUtils.get<WeatherNow>(url, param) { code, result ->
                weatherNow.value = result.now
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