package me.wsj.fengyun.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.wsj.fengyun.R;
import me.wsj.fengyun.bean.CityBean;
import me.wsj.fengyun.bean.CityBeanList;
import me.wsj.fengyun.dataInterface.DataUtil;
import me.wsj.fengyun.utils.ContentUtil;
import me.wsj.fengyun.utils.SpUtils;
import me.wsj.fengyun.view.activity.SearchActivity;

import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Range;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import java.util.ArrayList;
import java.util.List;


/**
 * 最近搜索
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityBean> data;
    private String searchText;

    private OnCityCheckedListener onCityCheckedListener;

    private Context mContext;

    public SearchAdapter(Context context, List<CityBean> data, String searchText) {
        mContext = context;
        this.data = data;
        this.searchText = searchText;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_searching, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        MyViewHolder viewHolder = (MyViewHolder) myViewHolder;
        View itemView = viewHolder.itemView;
        String name = data.get(i).getCityName();
        int x = name.indexOf("-");
        String parentCity = name.substring(0, x);
        String location = name.substring(x + 1);

        String cityName = location + "，" + parentCity + "，" + data.get(i).getAdminArea() + "，" + data.get(i).getCnty();
        if (TextUtils.isEmpty(data.get(i).getAdminArea())) {
            cityName = location + "，" + parentCity + "，" + data.get(i).getCnty();
        }
        if (!TextUtils.isEmpty(cityName)) {
            viewHolder.tvCity.setText(cityName);
            if (cityName.contains(searchText)) {
                int index = cityName.indexOf(searchText);
                //创建一个 SpannableString对象
                SpannableString sp = new SpannableString(cityName);
                //设置高亮样式一
                sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.light_text_color)), index, index + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tvCity.setText(sp);
            }
        }

        itemView.setOnClickListener(view -> {
            if(onCityCheckedListener!=null){
                onCityCheckedListener.onChecked(data.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCity;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_item_history_city);

        }
    }

    public void setOnCityCheckedListener(OnCityCheckedListener onCityCheckedListener) {
        this.onCityCheckedListener = onCityCheckedListener;
    }

    public interface OnCityCheckedListener{
        void onChecked(CityBean cityBean);
    }
}
