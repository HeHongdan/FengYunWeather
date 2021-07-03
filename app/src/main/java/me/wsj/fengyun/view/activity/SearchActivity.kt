package me.wsj.fengyun.view.activity

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import me.wsj.fengyun.adapter.SearchAdapter
import me.wsj.fengyun.bean.CityBean
import me.wsj.fengyun.bean.Location
import me.wsj.fengyun.databinding.ActivitySearchBinding
import me.wsj.fengyun.extension.startActivity
import me.wsj.fengyun.view.activity.vm.SearchViewModel
import me.wsj.fengyun.view.base.BaseActivity
import java.util.*

class SearchActivity : BaseActivity<ActivitySearchBinding>() {


    private val viewModel: SearchViewModel by viewModels()

    private var adapter: SearchAdapter? = null

    private val mData by lazy { ArrayList<CityBean>() }

    private var fromSplash = false

    override fun bindView() = ActivitySearchBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent) {
        fromSplash = intent.getBooleanExtra("fromSplash", false)
    }

    override fun initView() {
        setTitle("添加城市")
        mBinding.etSearch.threshold = 2


        adapter = SearchAdapter(
            this@SearchActivity,
            mData,
            mBinding.etSearch.text.toString()
        )
        mBinding.rvSearch.adapter = adapter
    }

    override fun initEvent() {
        //编辑框输入监听
        mBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keywords = mBinding.etSearch.text.toString()
                if (!TextUtils.isEmpty(keywords)) {
                    mBinding.llHistory.visibility = View.GONE
                    mBinding.rvSearch.visibility = View.VISIBLE
                    viewModel.searchCity(keywords)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        adapter?.setOnCityCheckedListener {
            viewModel.addCity(it)
            if (fromSplash) {
                startActivity<HomeActivity>()
            }
            finish()
        }

        viewModel.searchResult.observe(this) {
            showSearchResult(it)
        }

        viewModel.topCity.observe(this) {

        }

        mBinding.etSearch.adapter
    }

    private fun showSearchResult(basic: List<Location>) {
        mData.clear()

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
            mData.add(cityBean)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun initData() {

    }

    companion object {
        fun startActivity(context: Context, fromSplash: Boolean = false) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("fromSplash", fromSplash)
            context.startActivity(intent)
        }
    }
}