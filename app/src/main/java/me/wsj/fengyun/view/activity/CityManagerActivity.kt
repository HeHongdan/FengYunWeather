package me.wsj.fengyun.view.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import me.wsj.fengyun.R
import me.wsj.fengyun.adapter.CityManagerAdapter
import me.wsj.fengyun.adapter.MyItemTouchCallback
import me.wsj.fengyun.databinding.ActivityCityManagerBinding
import me.wsj.fengyun.db.entity.CityEntity
import me.wsj.fengyun.utils.ContentUtil
import me.wsj.fengyun.view.activity.vm.CityManagerViewModel
import me.wsj.fengyun.view.base.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class CityManagerActivity : BaseActivity<ActivityCityManagerBinding>() {

    private val datas by lazy { ArrayList<CityEntity>() }

    private val viewModel by viewModels<CityManagerViewModel>()

    private var adapter: CityManagerAdapter? = null

    @Inject
    lateinit var itemTouchCallback: MyItemTouchCallback

    override fun bindView() = ActivityCityManagerBinding.inflate(layoutInflater)

    override fun prepareData(intent: Intent?) {
    }

    override fun initView() {
        setTitle(getString(R.string.control_city))

        adapter = CityManagerAdapter(datas) {
            // todo 更新城市排序
        }

        mBinding.recycleControl.adapter = adapter

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(mBinding.recycleControl)
    }

    override fun initEvent() {
        adapter?.listener = object : CityManagerAdapter.OnCityRemoveListener {
            override fun onCityRemove(pos: Int) {
                viewModel.removeCity(datas[pos].cityId)
                datas.removeAt(pos)
                adapter?.notifyDataSetChanged()
                ContentUtil.CITY_CHANGE = true
            }
        }

        viewModel.cities.observe(this) {
            datas.clear()
            datas.addAll(it)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun initData() {
        viewModel.getCities()

    }
}