package me.wsj.fengyun.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.CityBean

/**
 * 最近搜索城市(区，城市，省，国家)的适配器。
 *
 * @property mContext 上下文。
 * @property data 搜索城市的集合。
 * @property searchText 搜索的文本。
 * @property onCityChecked 选中城市时回调。
 * @constructor 搜索城市对象。
 */
class SearchAdapter(
    private val mContext: Context,
    private val data: List<CityBean>,
    private val searchText: String,
    private val onCityChecked: (CityBean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_searching, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        myViewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val viewHolder = myViewHolder as MyViewHolder
        val item = data[position]
        val name = item.cityName
        val x = name.indexOf("-")
        val parentCity = name.substring(0, x)
        val location = name.substring(x + 1)
        var cityName = location + "，" + parentCity + "，" + item.adminArea + "，" + item.cnty
        if (TextUtils.isEmpty(item.adminArea)) {
            cityName = location + "，" + parentCity + "，" + item.cnty
        }
        if (!TextUtils.isEmpty(cityName)) {
            viewHolder.tvCity.text = cityName
            if (cityName.contains(searchText)) {
                val index = cityName.indexOf(searchText)
                //创建一个 SpannableString对象
                val sp = SpannableString(cityName)
                //设置高亮样式一
                sp.setSpan(
                    ForegroundColorSpan(mContext.resources.getColor(R.color.light_text_color)),
                    index,
                    index + searchText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                viewHolder.tvCity.text = sp
            }
        }
        viewHolder.itemView.setOnClickListener { view: View? ->
            onCityChecked(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity = itemView.findViewById<TextView>(R.id.tv_item_history_city)
    }
}