package me.wsj.fengyun.view.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import me.wsj.fengyun.adapter.SearchAdapter
import me.wsj.fengyun.adapter.TopCityAdapter
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.databinding.ActivityAddCityBinding
import me.wsj.fengyun.extension.showToast
import me.wsj.fengyun.extension.startActivity
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.utils.expand
import me.wsj.fengyun.view.activity.vm.SearchViewModel
import me.wsj.fengyun.view.base.BaseActivity
import me.wsj.fengyun.view.base.LoadState
import java.util.*

class AddCityActivity : BaseActivity<ActivityAddCityBinding>() {


    private val viewModel: SearchViewModel by viewModels()

    private var searchAdapter: SearchAdapter? = null

    private var topCityAdapter: TopCityAdapter? = null

    private val searchCities by lazy { ArrayList<CityBean>() }

    private val topCities by lazy { ArrayList<CityBean>() }

    private var fromSplash = false

    override fun bindView() = ActivityAddCityBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent) {
        fromSplash = intent.getBooleanExtra("fromSplash", false)
    }

    override fun initView() {
        setTitle("添加城市")
        mBinding.etSearch.threshold = 2

        searchAdapter = SearchAdapter(
            this@AddCityActivity,
            searchCities,
            mBinding.etSearch.text.toString()
        )
        mBinding.rvSearch.adapter = searchAdapter

        topCityAdapter = TopCityAdapter(topCities)
        val layoutManager = GridLayoutManager(context, 3)
        mBinding.rvTopCity.adapter = topCityAdapter
        mBinding.rvTopCity.layoutManager = layoutManager

        topCityAdapter?.listener = SearchAdapter.OnCityCheckedListener {
            viewModel.addCity(it)
        }

        mBinding.tvGetPos.expand(10, 10)
    }

    override fun initEvent() {
        //编辑框输入监听
        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keywords = mBinding.etSearch.text.toString()
                if (!TextUtils.isEmpty(keywords)) {
                    viewModel.searchCity(keywords)
                } else {
                    mBinding.rvSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        mBinding.tvGetPos.setOnClickListener {
            showToast("定位")
        }

        searchAdapter?.setOnCityCheckedListener {
            viewModel.addCity(it)
        }

        viewModel.loadState.observe(this) {
            when (it) {
                is LoadState.Start -> {
                    showLoading(true)
                }
                is LoadState.Error -> {

                }
                is LoadState.Finish -> {
                    showLoading(false)
                }
            }
        }

        viewModel.addFinish.observe(this) {
            if (it) {
                if (fromSplash) {
                    startActivity<HomeActivity>()
                }
                ContentUtil.CITY_CHANGE = true
                finish()
            }
        }

        viewModel.searchResult.observe(this) {
            showSearchResult(it)
        }

        viewModel.topCity.observe(this) {
            showTopCity(it)
        }
    }

    private fun showTopCity(locations: List<Location>) {
        topCities.clear()
        locations.forEach { item ->
            var parentCity = item.adm2
            val adminArea = item.adm1
            val city = item.country
            if (TextUtils.isEmpty(parentCity)) {
                parentCity = adminArea
            }
            if (TextUtils.isEmpty(adminArea)) {
                parentCity = city
            }
            val cityBean = CityBean()
            cityBean.cityName = parentCity + " - " + item.name
            cityBean.cityId = item.id
            cityBean.cnty = city
            cityBean.adminArea = adminArea
            topCities.add(cityBean)
        }
        topCityAdapter?.notifyDataSetChanged()
    }

    private fun showSearchResult(basic: List<Location>) {
        mBinding.rvSearch.visibility = View.VISIBLE

        searchCities.clear()

        basic.forEach { item ->
            var parentCity = item.adm2
            val adminArea = item.adm1
            val city = item.country
            if (TextUtils.isEmpty(parentCity)) {
                parentCity = adminArea
            }
            if (TextUtils.isEmpty(adminArea)) {
                parentCity = city
            }
            val cityBean = CityBean()
            cityBean.cityName = parentCity + " - " + item.name
            cityBean.cityId = item.id
            cityBean.cnty = city
            cityBean.adminArea = adminArea
            searchCities.add(cityBean)
        }
        searchAdapter?.notifyDataSetChanged()
    }

    override fun initData() {
        viewModel.getTopCity()
    }

    companion object {
        fun startActivity(context: Context, fromSplash: Boolean = false) {
            val intent = Intent(context, AddCityActivity::class.java)
            intent.putExtra("fromSplash", fromSplash)
            context.startActivity(intent)
        }
    }
}