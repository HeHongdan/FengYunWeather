package me.wsj.fengyun.ui.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import me.wsj.fengyun.BuildConfig
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.bean.SearchCity
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.ui.base.BaseViewModel
import me.wsj.lib.net.HttpUtils
import me.wsj.lib.net.LoadState
import java.util.*
import kotlin.collections.HashMap

/** 最后定位(城市)。 */
const val LAST_LOCATION = "LAST_LOCATION"

class SearchViewModel(private val app: Application) : BaseViewModel(app) {

    /** (和风天气)搜索结果(定位的城市)。 */
    val searchResult = MutableLiveData<List<Location>>()
    /** 当前的城市。 */
    val curCity = MutableLiveData<Location>()
    /** 选中的城市。 */
    val choosedCity = MutableLiveData<Location>()
    /** 热门的城市。 */
    val topCity = MutableLiveData<List<String>>()
    /** 是否添加(城市)完成(双向绑定)。 */
    val addFinish = MutableLiveData<Boolean>()


    /**
     * 搜索城市(协程)。
     *
     * @param keywords 搜索关键字。
     */
    fun searchCity(keywords: String) {
        launchSilent {
            val url = "https://geoapi.qweather.com/v2/city/lookup"
            val param = HashMap<String, Any>()
            param["location"] = keywords
            param["key"] = BuildConfig.HeFengKey

            HttpUtils.get<SearchCity>(url, param) { _, result ->
                searchResult.value = result.location
            }
//            val result = HttpUtils.get<SearchCity>(url, param)
        }
    }

    /**
     * 获取城市数据(协程)。
     *
     * @param cityName 城市名称。
     * @param save 是否保存。
     */
    fun getCityInfo(cityName: String, save: Boolean = false) {
        launch {
            if (save) {
                AppRepo.getInstance().saveCache(LAST_LOCATION, cityName)// 缓存定位城市
            }

            val url = "https://geoapi.qweather.com/v2/city/lookup"
            val param = HashMap<String, Any>()
            param["location"] = cityName
            param["key"] = BuildConfig.HeFengKey

            HttpUtils.get<SearchCity>(url, param) { _, result ->
                if (save) {
                    curCity.value = result.location[0]
                } else {
                    choosedCity.value = result.location[0]
                }
            }
        }
    }

    /**
     * 获取热门城市
     */
    fun getTopCity() {
        launch {
            /*var topCityCache = AppRepo.getInstance().getCache<ArrayList<Location>>("top_city")
            if (topCityCache != null && topCityCache.isNotEmpty()) {
                topCity.postValue(topCityCache!!)
                return@launch
            }

            val url = "https://geoapi.qweather.com/v2/city/top"
            val param = HashMap<String, Any>()
            param["key"] = Constants.APK_KEY
            param["number"] = 20

            HttpUtils.get<TopCity>(url, param) { code, result ->
                topCity.value = result.topCityList
                launch {
                    AppRepo.getInstance().saveCache("top_city", result.topCityList)
                }
            }*/
            val stringArray = app.resources.getStringArray(R.array.top_city)
            val cityList = stringArray.toList() as ArrayList<String>
            topCity.postValue(cityList)
        }
    }

    /**
     * 添加城市(协程)。
     *
     * @param it 城市对象。
     * @param isLocal 是否本地。
     */
    fun addCity(it: CityBean, isLocal: Boolean = false) {
        launch {
            AppRepo.getInstance().addCity(CityEntity(it.cityId, it.cityName, isLocal))//协程添加城市到数据库
            addFinish.postValue(true)
        }
    }

    val curLocation = MutableLiveData<String>()

    /**
     * 开始获取定位信息。
     */
    fun getLocation() {
        loadState.postValue(LoadState.Start("正在获取位置信息..."))
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
                curLocation.value = aMapLocation.district
            } else {
                loadState.value = LoadState.Error("获取定位失败,请重试")
            }
            loadState.value = LoadState.Finish
            mLocationClient.onDestroy()
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    /** 获取缓存的定位。 */
    val cacheLocation = MutableLiveData<String>()

    /**
     * 获取缓存的定位(协程)。
     */
    fun getCacheLocation() {
        launch {
            (AppRepo.getInstance().getCache<String>(LAST_LOCATION))?.let {
                cacheLocation.postValue(it)
            }
        }
    }
}