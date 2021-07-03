package me.wsj.fengyun.view.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Range
import com.qweather.sdk.bean.geo.GeoBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.view.base.BaseViewModel
import per.wsj.commonlib.utils.LogUtil
import java.util.*

class MainViewModel(val app: Application) : BaseViewModel(app) {

    val curCid = MutableLiveData<String>()

    val condCode = MutableLiveData<String>()

    val locationBean = MutableLiveData<GeoBean.LocationBean>()

    val cityBean = MutableLiveData<List<CityBean>>()

    val obtainLocation = MutableLiveData<Boolean>()

    fun initLocation() {
        LogUtil.LOGE("initLoacation")
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
                // 获取city
                ContentUtil.NOW_LON = aMapLocation.longitude
                ContentUtil.NOW_LAT = aMapLocation.latitude
                obtainLocation.value = true
            } else {
                obtainLocation.value = false
                /*if (!PermissionUtil.hasPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {
                    // 没有权限
                    val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.pop_loc_list, null)
                    val locListWindow = LocListWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, this@MainActivity)
                    locListWindow.show()
                    locListWindow.showAtLocation(mBinding.tvLocation, Gravity.CENTER, 0, 0)
                    if (ContentUtil.FIRST_OPEN) {
                        ContentUtil.FIRST_OPEN = false
                        SpUtils.putBoolean(this@MainActivity, "first_open", false)
                    }
                }*/
            }

            mLocationClient.onDestroy()
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()


    }

    /**
     * 根据经纬度获取城市信息
     */
    fun getNow(location: String, nowCity: Boolean) {
//        LogUtil.LOGE("getNow() -> " + location)
        QWeather.getGeoCityLookup(
                app,
                location,
                Range.WORLD,
                3,
                Lang.ZH_HANS,
                object : QWeather.OnResultGeoListener {
                    override fun onError(throwable: Throwable) {}
                    override fun onSuccess(search: GeoBean) {
                        val basic = search.locationBean[0]
                        val cid = basic.id
                        val location = basic.name
                        if (nowCity) {
                            ContentUtil.NOW_CITY_ID = cid
                            ContentUtil.NOW_CITY_NAME = location
                            curCid.value = cid
                        }
                    }
                })
    }


    fun getWeatherByCid(cid: String) {
        QWeather.getWeatherNow(
                app,
                cid,
                object : QWeather.OnResultWeatherNowListener {
                    override fun onError(throwable: Throwable) {}
                    override fun onSuccess(weatherNowBean: WeatherNowBean) {
                        val code = weatherNowBean.code.code
                        if (Code.OK.code.equals(code, ignoreCase = true)) {
                            val now = weatherNowBean.now
                            condCode.value = now.icon
                        }
                    }
                })
    }

    /**
     * 根据经纬度获取城市信息
     */
    fun getCityByCoordinate() {
        val lang = getLang()

        QWeather.getGeoCityLookup(app, ContentUtil.NOW_LON.toString() + "," + ContentUtil.NOW_LAT, Range.WORLD, 3, lang, object : QWeather.OnResultGeoListener {
            override fun onError(throwable: Throwable) {
                val cityBeans: MutableList<CityBean> = ArrayList()
                val city = CityBean()
                city.cityName = "北京"
                city.cityId = "CN101010100"
                cityBeans.add(city)
                cityBean.value = cityBeans
            }

            override fun onSuccess(search: GeoBean) {
                val basic = search.locationBean[0]
                locationBean.value = basic
            }
        })
    }

    private fun getLang() = Lang.ZH_HANS
}