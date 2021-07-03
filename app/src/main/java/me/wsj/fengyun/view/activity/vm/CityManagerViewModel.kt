package me.wsj.fengyun.view.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.wsj.fengyun.db.AppRepo
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.view.base.BaseViewModel

class CityManagerViewModel(private val app: Application) : BaseViewModel(app) {

    val cities = MutableLiveData<List<CityEntity>>()

    fun getCities() {
        launch {
            val results = AppRepo.getInstance().getCities()
            cities.postValue(results)
        }
    }

    fun removeCity(cityId:String) {
        launch {
            AppRepo.getInstance().removeCity(cityId)
        }
    }
}