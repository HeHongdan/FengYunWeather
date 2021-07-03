package me.wsj.fengyun.view.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.bean.SearchCity
import me.wsj.fengyun.bean.TopCity
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.view.base.BaseViewModel
import me.wsj.lib.Constants
import me.wsj.lib.net.HttpUtils

class SearchViewModel(private val app: Application) : BaseViewModel(app) {

    val searchResult = MutableLiveData<List<Location>>()
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

    fun getHostCity() {
        launch {
            val url = "https://geoapi.qweather.com/v2/city/top"
            val param = HashMap<String, Any>()
            param["key"] = Constants.APK_KEY

            HttpUtils.get<TopCity>(url, param) {
                topCity.value = it.topCityList
            }
        }
    }

    fun addCity(it: CityBean) {
        launch {
            AppRepo.getInstance().addCity(CityEntity(it.cityId, it.cityName))
            addFinish.postValue(true)
        }
    }
}