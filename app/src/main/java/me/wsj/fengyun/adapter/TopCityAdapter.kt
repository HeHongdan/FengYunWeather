package me.wsj.fengyun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R

class TopCityAdapter(val mData: List<String>, val onChecked: (String) -> Unit) :
    RecyclerView.Adapter<TopCityAdapter.ViewHolder>() {

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
        holder.tvCityName.text = item

        holder.itemView.setOnClickListener {
            onChecked(item)
        }
    }

    override fun getItemCount() = mData.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCityName = itemView.findViewById<TextView>(R.id.tvCityName)
    }
}