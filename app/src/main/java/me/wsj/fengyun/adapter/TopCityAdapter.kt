package me.wsj.fengyun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.CityBean

class TopCityAdapter(val mData: List<CityBean>) :
    RecyclerView.Adapter<TopCityAdapter.ViewHolder>() {

    var listener: SearchAdapter.OnCityCheckedListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_top_city, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.tvCityName.text = item.cityName

        holder.itemView.setOnClickListener {
            listener?.onChecked(item)
        }
    }

    override fun getItemCount() = mData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCityName = itemView.findViewById<TextView>(R.id.tvCityName)
    }
}