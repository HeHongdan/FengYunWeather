package me.wsj.fengyun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.DateTime;

import java.util.List;

import me.wsj.fengyun.R;
import me.wsj.fengyun.bean.Daily;
import me.wsj.fengyun.utils.IconUtils;
import me.wsj.fengyun.utils.WeatherUtil;


public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    private List<Daily> datas;
    private Context context;

    public ForecastAdapter(Context context, List<Daily> datas) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_forecast, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void refreshData(Context context, List<Daily> datas) {
        this.datas = datas;
        this.context = context;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        Daily forecastBase = datas.get(i);
        String condCodeD = forecastBase.getIconDay();
        String condCodeN = forecastBase.getIconNight();
        String tmpMin = forecastBase.getTempMin();
        String tmpMax = forecastBase.getTempMax();
        myViewHolder.tvMax.setText(tmpMax + "°");
        myViewHolder.tvMin.setText(tmpMin + "°");
        myViewHolder.ivDay.setImageResource(IconUtils.getDayIconDark(context, condCodeD));
        myViewHolder.ivNight.setImageResource(IconUtils.getDayIconDark(context, condCodeN));
        DateTime now = DateTime.now();
        myViewHolder.tvWeek.setText(
                WeatherUtil.getWeek(now.plusDays(i).getDayOfWeek()));
        myViewHolder.tvWeek.setTextColor(context.getResources().getColor(R.color.edit_hint_color));
        if (i == 0) {
            myViewHolder.tvWeek.setText(context.getString(R.string.today));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivDay;
        private final ImageView ivNight;
        private final TextView tvWeek;
        private final TextView tvMin;
        private final TextView tvMax;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDay = itemView.findViewById(R.id.iv_day);
            ivNight = itemView.findViewById(R.id.iv_night);
            tvWeek = itemView.findViewById(R.id.tv_week);
            tvMin = itemView.findViewById(R.id.tv_min);
            tvMax = itemView.findViewById(R.id.tv_max);
        }
    }
}
