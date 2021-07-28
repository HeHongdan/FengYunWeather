package me.wsj.fengyun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.wsj.fengyun.R
import me.wsj.fengyun.bean.Daily
import me.wsj.lib.utils.IconUtils

class Forecast3dAdapter(val context: Context, val datas: List<Daily>) :
    RecyclerView.Adapter<Forecast3dAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datas[position]
        holder.tvTemp.text = "${item.tempMin}~${item.tempMax}°C"

        var desc = item.textDay
        if (item.textDay != item.textNight) {
            desc += "转" + item.textNight
        }
        holder.tvDesc.text = desc

        when (position) {
            0 -> {
                holder.tvWeek.text = context.getString(R.string.today)
                if (IconUtils.isDay()) {
                    holder.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
                } else {
                    holder.ivDay.setImageResource(IconUtils.getNightIconDark(context, item.iconDay))
                }
            }
            1 -> {
                holder.tvWeek.text = context.getString(R.string.tomorrow)
                holder.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
            }
            else -> {
                holder.tvWeek.text = context.getString(R.string.after_t)
                holder.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))
            }
        }
    }

    override fun getItemCount(): Int = 3

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDay = itemView.findViewById<ImageView?>(R.id.iv_day)

        val tvWeek = itemView.findViewById<TextView?>(R.id.tv_week)
        val tvTemp = itemView.findViewById<TextView?>(R.id.tvTemp)
        val tvDesc = itemView.findViewById<TextView?>(R.id.tvDesc)
    }
}