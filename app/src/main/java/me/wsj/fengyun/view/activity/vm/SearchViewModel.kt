package me.wsj.fengyun.view.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.bean.SearchCity
import me.wsj.fengyun.bean.TopCity
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.view.base.BaseViewModel
import me.wsj.fengyun.view.base.LoadState
import me.wsj.lib.Constants
import me.wsj.lib.net.HttpUtils
import per.wsj.commonlib.utils.LogUtil

class SearchViewModel(private val app: Application) : BaseViewModel(app) {

    val searchResult = MutableLiveData<List<Location>>()

    val curCity = MutableLiveData<Location>()

    val topCity = MutableLiveData<List<Location>>()

    val addFinish = MutableLiveData<Boolean>()


    fun searchCity(keywords: String) {
        launchSilent {
            val url = "https://geoapi.qweather.com/v2/city/lookup"
            val param = HashMap<String, Any>()
            param["location"] = keywords
            param["key"] = Constants.APK_KEY

            HttpUtils.get<SearchCity>(url, param) {
                searchResult.value = it.location
            }
        }
    }

    fun getCityInfo(cityName: String) {
        launch {
            val url = "https://geoapi.qweather.com/v2/city/lookup"
            val param = HashMap<String, Any>()
            param["location"] = cityName
            param["key"] = Constants.APK_KEY

            HttpUtils.get<SearchCity>(url, param) {
                curCity.value = it.location[0]
            }
        }
    }

    fun getTopCity() {
        launch {
            var topCityCache = AppRepo.getInstance().getCache<ArrayList<Location>>("top_city")
            if (topCityCache != null && topCityCache.isNotEmpty()) {
                topCity.postValue(topCityCache!!)
                return@launch
            }

            val url = "https://geoapi.qweather.com/v2/city/top"
            val param = HashMap<String, Any>()
            param["key"] = Constants.APK_KEY
            param["number"] = 20

            HttpUtils.get<TopCity>(url, param) {
                topCity.value = it.topCityList
                launch {
                    AppRepo.getInstance().saveCache("top_city", it.topCityList)
                }
            }
        }
    }

    fun addCity(it: CityBean, isLocal: Boolean = false) {
        launch {
            AppRepo.getInstance().addCity(CityEntity(it.cityId, it.cityName, isLocal))
            addFinish.postValue(true)
        }
    }

    val curLocation = MutableLiveData<String>()

    fun getLocation() {
        loadState.postValue(LoadState.Start("正在获取定位"))
        //初始化定位
        val mLocationClient = AMapLocationClient(app)
        //设置定位回调监听

        //声明AMapLocationClientOption对象
        val mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.interval = 10000
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.httpTimeOut = 20000
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation.errorCode == 0) {
                curLocation.postValue(aMapLocation.city)
            } else {
                loadState.postValue(LoadState.Error("获取定位失败,请重试"))
            }
            loadState.postValue(LoadState.Finish)
            mLocationClient.onDestroy()
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }
}