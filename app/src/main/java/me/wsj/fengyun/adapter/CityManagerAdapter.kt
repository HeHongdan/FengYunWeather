package me.wsj.fengyun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R
import me.wsj.fengyun.db.entity.CityEntity

class CityManagerAdapter(val mData: List<CityEntity>) :
    RecyclerView.Adapter<CityManagerAdapter.ViewHolder>() {

    var listener: OnCityRemoveListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_follow_city, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.tvItemCity.text = item.cityName

        holder.itemView.setOnClickListener {
            listener?.onCityRemove(position)
        }
    }

    override fun getItemCount() = mData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDelete = itemView.findViewById<ImageView>(R.id.iv_item_delete)
        val tvItemCity = itemView.findViewById<TextView>(R.id.tv_item_city)
    }

    public interface OnCityRemoveListener {
        fun onCityRemove(pos: Int)
    }
}