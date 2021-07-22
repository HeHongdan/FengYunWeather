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
import java.util.*

class Forecast15dAdapter(val context: Context, val datas: List<Daily>) :
    RecyclerView.Adapter<Forecast15dAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_forecast15, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datas[position]
        holder.tvWeek.text = getWeekDay(position)
        holder.tvDate.text = item.fxDate.removeRange(IntRange(0, 4))
        holder.tvDayDesc.text = item.textDay
        holder.ivDay.setImageResource(IconUtils.getDayIconDark(context, item.iconDay))

        holder.ivNight.setImageResource(IconUtils.getNightIconDark(context, item.iconNight))
        holder.tvNightDesc.text = item.textNight
        holder.tvWind.text = item.windDirDay
        holder.tvWindScale.text = item.windScaleDay + "级"

    }

    val weeks = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    private fun getWeekDay(position: Int): String {
        if (position == 0) {
            return "今天"
        } else {
            val calendar = Calendar.getInstance()
            val dateArray = datas[position].fxDate.split("-")
            calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
            var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
            if (w < 0) {
                w = 0
            }
            return weeks[w]
        }
    }

    override fun getItemCount(): Int = datas.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWeek = itemView.findViewById<TextView?>(R.id.tv_week)
        val tvDate = itemView.findViewById<TextView?>(R.id.tv_date)
        val tvDayDesc = itemView.findViewById<TextView?>(R.id.tv_day_desc)
        val ivDay = itemView.findViewById<ImageView?>(R.id.iv_day)

        val tvNightDesc = itemView.findViewById<TextView?>(R.id.tv_night_desc)
        val ivNight = itemView.findViewById<ImageView?>(R.id.iv_night)

        val tvWind = itemView.findViewById<TextView?>(R.id.tv_wind)
        val tvWindScale = itemView.findViewById<TextView?>(R.id.tv_wind_scale)
    }
}