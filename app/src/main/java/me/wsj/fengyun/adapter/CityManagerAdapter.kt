package me.wsj.fengyun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R
import me.wsj.fengyun.db.entity.CityEntity
import java.util.*

class CityManagerAdapter(val mData: List<CityEntity>,
                         var onSort: ((List<CityEntity>) -> Unit)? = null) :
    RecyclerView.Adapter<CityManagerAdapter.ViewHolder>(), IDragSort {

    var listener: OnCityRemoveListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city_manager, parent, false)
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

    override fun move(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun dragFinish() {
        onSort?.let { it(mData) }
    }
}