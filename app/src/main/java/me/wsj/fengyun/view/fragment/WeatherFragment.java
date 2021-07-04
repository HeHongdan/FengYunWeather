package me.wsj.fengyun.view.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qweather.sdk.bean.WarningBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import me.wsj.fengyun.R;
import me.wsj.fengyun.adapter.ForecastAdapter;
import me.wsj.fengyun.databinding.FragmentWeatherBinding;
import me.wsj.fengyun.databinding.LayoutTodayDetailBinding;
import me.wsj.fengyun.presenters.WeatherInterface;
import me.wsj.fengyun.presenters.impl.WeatherImpl;
import me.wsj.fengyun.utils.ContentUtil;
import me.wsj.fengyun.utils.IconUtils;
import me.wsj.fengyun.utils.TransUnitUtil;
import me.wsj.fengyun.view.base.BaseFragment;
import me.wsj.fengyun.widget.horizonview.ScrollWatched;
import me.wsj.fengyun.widget.horizonview.ScrollWatcher;
import per.wsj.commonlib.utils.LogUtil;

public class WeatherFragment extends BaseFragment<FragmentWeatherBinding> implements WeatherInterface {
    private static final String PARAM_CITY_ID = "param_city_id";
    List<ScrollWatcher> watcherList = new ArrayList<>();

    private ScrollWatched watched;

    private String currentTime;
    private String sunrise;
    private String sunset;
    private String moonRise;
    private String moonSet;
    private boolean hasAni = false;

    private String todayMaxTmp;
    private String todayMinTmp;
    private WeatherDailyBean weatherForecastBean;
    private WeatherHourlyBean weatherHourlyBean;
    private String nowTmp;
    private String mCityId;

    private String condCode;

    private ForecastAdapter forecastAdapter;

    private LayoutTodayDetailBinding todayDetailBinding;

    private WeatherViewModel viewModel;

    public static WeatherFragment newInstance(String cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCityId = getArguments().getString(PARAM_CITY_ID);
        }
    }


    @Override
    protected FragmentWeatherBinding bindView() {
        return FragmentWeatherBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView(View view) {
        viewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        todayDetailBinding = LayoutTodayDetailBinding.bind(mBinding.getRoot());

        getCurrentTime();

        mBinding.horizontalScrollView.setToday24HourView(mBinding.hourly);

        //横向滚动监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    watched.notifyWatcher(scrollX);
                }
            });
        }

        mBinding.swipeLayout.setOnRefreshListener(() -> initData(mCityId));
    }

    @Override
    public void initEvent() {
        watched = new ScrollWatched() {
            @Override
            public void addWatcher(ScrollWatcher watcher) {
                watcherList.add(watcher);
            }

            @Override
            public void removeWatcher(ScrollWatcher watcher) {
                watcherList.remove(watcher);
            }

            @Override
            public void notifyWatcher(int x) {
                for (ScrollWatcher watcher : watcherList) {
                    watcher.update(x);
                }
            }
        };

        watched.addWatcher(mBinding.hourly);
    }

    @Override
    public void loadData() {
        initData(mCityId);

        if (!hasAni && !TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            setViewTime();
        }

        if (!TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            setViewTime();
        }

        getWeatherForecast(weatherForecastBean);
    }

    private void initData(String cityId) {
        WeatherImpl weatherImpl = new WeatherImpl(this.getActivity(), this);
        weatherImpl.getWeatherHourly(cityId);
        weatherImpl.getAirForecast(cityId);
        weatherImpl.getAirNow(cityId);
        weatherImpl.getWarning(cityId);
        weatherImpl.getWeatherForecast(cityId);
        weatherImpl.getWeatherNow(cityId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        LogUtil.e("-----------------onResume------------------" + mCityId);
        if (condCode != null) {
            viewModel.setCondCode(condCode);
            LogUtil.e("-----------------ch bg------------------ ch bg" + mCityId);
        }

        if (!hasAni && !TextUtils.isEmpty(sunrise) && !TextUtils.isEmpty(sunset) && !TextUtils.isEmpty(moonRise) && !TextUtils.isEmpty(moonSet)) {
            setViewTime();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e("-----------------onDetach------------------" + mCityId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("-----------------onDestroy------------------" + mCityId);
    }

    @SuppressLint("SetTextI18n")
    public void changeUnit() {
        if (todayDetailBinding.tvMaxTmp != null) {
            if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                LogUtil.e("当前城市1：" + condCode);
                todayDetailBinding.tvMaxTmp.setText(TransUnitUtil.getF(todayMaxTmp) + "°F");
                todayDetailBinding.tvMinTmp.setText(TransUnitUtil.getF(todayMinTmp) + "°F");
                mBinding.tvTodayTmp.setText(TransUnitUtil.getF(nowTmp) + "°F");
            } else {
                LogUtil.e("当前城市2：" + condCode);
                todayDetailBinding.tvMaxTmp.setText(todayMaxTmp + "°C");
                todayDetailBinding.tvMinTmp.setText(todayMinTmp + "°C");
                mBinding.tvTodayTmp.setText(nowTmp + "°C");
            }
        }
        getWeatherHourly(weatherHourlyBean);
        getWeatherForecast(weatherForecastBean);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherNow(WeatherNowBean bean) {
        if (bean != null && bean.getNow() != null) {
            WeatherNowBean.NowBaseBean now = bean.getNow();
            String rain = now.getPrecip();
            String hum = now.getHumidity();
            String pres = now.getPressure();
            String vis = now.getVis();
            String windDir = now.getWindDir();
            String windSc = now.getWindScale();
            String condTxt = now.getText();
            condCode = now.getIcon();
            viewModel.setCondCode(condCode);

            nowTmp = now.getTemp();
            mBinding.tvTodayCond.setText(condTxt);
            mBinding.tvTodayTmp.setText(nowTmp + "°C");
            if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                mBinding.tvTodayTmp.setText(TransUnitUtil.getF(nowTmp) + "°F");
            }
            todayDetailBinding.tvTodayRain.setText(rain + "mm");
            todayDetailBinding.tvTodayPressure.setText(pres + "HPA");
            todayDetailBinding.tvTodayHum.setText(hum + "%");
            todayDetailBinding.tvTodayVisible.setText(vis + "KM");
            todayDetailBinding.tvWindDir.setText(windDir);
            todayDetailBinding.tvWindSc.setText(windSc + "级");
            DateTime nowTime = DateTime.now();
            int hourOfDay = nowTime.getHourOfDay();
            if (hourOfDay > 6 && hourOfDay < 19) {
                mBinding.ivBack.setImageResource(IconUtils.getDayBack(getContext(), condCode));
            } else {
                mBinding.ivBack.setImageResource(IconUtils.getNightBack(getContext(), condCode));
            }

            mBinding.swipeLayout.setRefreshing(false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherForecast(WeatherDailyBean bean) {
        if (bean != null && bean.getDaily() != null) {
            weatherForecastBean = bean;
            getCurrentTime();

            List<WeatherDailyBean.DailyBean> daily_forecast = bean.getDaily();

            WeatherDailyBean.DailyBean forecastBase = daily_forecast.get(0);
            String condCodeD = forecastBase.getIconDay();
            String condCodeN = forecastBase.getIconNight();
            String tmpMin = forecastBase.getTempMin();
            String tmpMax = forecastBase.getTempMax();
            sunrise = forecastBase.getSunrise();
            sunset = forecastBase.getSunset();
            moonRise = forecastBase.getMoonRise();
            moonSet = forecastBase.getMoonSet();
            todayDetailBinding.sunView.setTimes(sunrise, sunset, currentTime);
            todayDetailBinding.moonView.setTimes(moonRise, moonSet, currentTime);
            todayMaxTmp = tmpMax;
            todayMinTmp = tmpMin;
            todayDetailBinding.tvMaxTmp.setText(tmpMax + "°");
            todayDetailBinding.tvMinTmp.setText(tmpMin + "°");
            todayDetailBinding.ivTodayDay.setImageResource(IconUtils.getDayIconDark(getContext(), condCodeD));
            todayDetailBinding.ivTodayNight.setImageResource(IconUtils.getNightIconDark(getContext(), condCodeN));

            if (forecastAdapter == null) {
                forecastAdapter = new ForecastAdapter(getActivity(), daily_forecast);
                mBinding.rvForecast.setAdapter(forecastAdapter);
                LinearLayoutManager forecastManager = new LinearLayoutManager(getActivity());
                forecastManager.setOrientation(LinearLayoutManager.VERTICAL);
                mBinding.rvForecast.setLayoutManager(forecastManager);
            } else {
                forecastAdapter.refreshData(getActivity(), daily_forecast);
            }

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getWarning(WarningBean.WarningBeanBase alarmBase) {
        if (alarmBase != null) {
            mBinding.tvTodayAlarm.setVisibility(View.VISIBLE);
            String level = alarmBase.getLevel();
            String type = alarmBase.getType();
            mBinding.tvTodayAlarm.setText(type + "预警");

            if (!TextUtils.isEmpty(level)) {
                switch (level) {
                    case "蓝色":
                    case "Blue":
                        mBinding.tvTodayAlarm.setBackground(getResources().getDrawable(R.drawable.shape_blue_alarm));
                        mBinding.tvTodayAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "黄色":
                    case "Yellow":
                        mBinding.tvTodayAlarm.setBackground(getResources().getDrawable(R.drawable.shape_yellow_alarm));
                        mBinding.tvTodayAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "橙色":
                    case "Orange":
                        mBinding.tvTodayAlarm.setBackground(getResources().getDrawable(R.drawable.shape_orange_alarm));
                        mBinding.tvTodayAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "红色":
                    case "Red":
                        mBinding.tvTodayAlarm.setBackground(getResources().getDrawable(R.drawable.shape_red_alarm));
                        mBinding.tvTodayAlarm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "白色":
                    case "White":
                        mBinding.tvTodayAlarm.setBackground(getResources().getDrawable(R.drawable.shape_white_alarm));
                        mBinding.tvTodayAlarm.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
            }
        } else {
            mBinding.tvTodayAlarm.setVisibility(View.GONE);
        }
    }

    @Override
    public void getAirNow(AirNowBean bean) {
        if (bean != null && bean.getNow() != null) {
            todayDetailBinding.ivLine2.setVisibility(View.VISIBLE);
            todayDetailBinding.gridAir.setVisibility(View.VISIBLE);
            todayDetailBinding.rvAir.setVisibility(View.VISIBLE);
            todayDetailBinding.airTitle.setVisibility(View.VISIBLE);
            AirNowBean.NowBean airNowCity = bean.getNow();
            String qlty = airNowCity.getCategory();
            String aqi = airNowCity.getAqi();
            String pm25 = airNowCity.getPm2p5();
            String pm10 = airNowCity.getPm10();
            String so2 = airNowCity.getSo2();
            String no2 = airNowCity.getNo2();
            String co = airNowCity.getCo();
            String o3 = airNowCity.getO3();
            todayDetailBinding.tvAir.setText(qlty);
            todayDetailBinding.tvAirNum.setText(aqi);
            todayDetailBinding.tvTodayPm25.setText(pm25);
            todayDetailBinding.tvTodayPm10.setText(pm10);
            todayDetailBinding.tvTodaySo2.setText(so2);
            todayDetailBinding.tvTodayNo2.setText(no2);
            todayDetailBinding.tvTodayCo.setText(co);
            todayDetailBinding.tvTodayO3.setText(o3);
            todayDetailBinding.rvAir.setBackground(viewModel.getAirBackground(aqi));
        } else {
            todayDetailBinding.ivLine2.setVisibility(View.GONE);
            todayDetailBinding.gridAir.setVisibility(View.GONE);
            todayDetailBinding.rvAir.setVisibility(View.GONE);
            todayDetailBinding.airTitle.setVisibility(View.GONE);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void getWeatherHourly(WeatherHourlyBean bean) {
        if (bean != null && bean.getHourly() != null) {
            weatherHourlyBean = bean;
            List<WeatherHourlyBean.HourlyBean> hourlyWeatherList = bean.getHourly();
            List<WeatherHourlyBean.HourlyBean> data = new ArrayList<>();
            if (hourlyWeatherList.size() > 23) {
                for (int i = 0; i < 24; i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            } else {
                for (int i = 0; i < hourlyWeatherList.size(); i++) {
                    data.add(hourlyWeatherList.get(i));
                    String condCode = data.get(i).getIcon();
                    String time = data.get(i).getFxTime();
                    time = time.substring(time.length() - 11, time.length() - 9);
                    int hourNow = Integer.parseInt(time);
                    if (hourNow >= 6 && hourNow <= 19) {
                        data.get(i).setIcon(condCode + "d");
                    } else {
                        data.get(i).setIcon(condCode + "n");
                    }
                }
            }

            int minTmp = Integer.parseInt(data.get(0).getTemp());
            int maxTmp = minTmp;
            for (int i = 0; i < data.size(); i++) {
                int tmp = Integer.parseInt(data.get(i).getTemp());
                minTmp = Math.min(tmp, minTmp);
                maxTmp = Math.max(tmp, maxTmp);
            }
            //设置当天的最高最低温度
            mBinding.hourly.setHighestTemp(maxTmp);
            mBinding.hourly.setLowestTemp(minTmp);
            if (maxTmp == minTmp) {
                mBinding.hourly.setLowestTemp(minTmp - 1);
            }
            mBinding.hourly.initData(data);
            mBinding.tvLineMaxTmp.setText(maxTmp + "°");
            mBinding.tvLineMinTmp.setText(minTmp + "°");
            if (ContentUtil.APP_SETTING_UNIT.equals("hua")) {
                mBinding.tvLineMaxTmp.setText(TransUnitUtil.getF(String.valueOf(maxTmp)) + "°");
                mBinding.tvLineMinTmp.setText(TransUnitUtil.getF(String.valueOf(minTmp)) + "°");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding.getRoot() != null && mBinding.getRoot().getParent() != null) {
            ((ViewGroup) mBinding.getRoot().getParent()).removeView(mBinding.getRoot());
        }
    }

    /**
     * 设置view的时间
     */
    private void setViewTime() {
        getCurrentTime();

        todayDetailBinding.sunView.setTimes(sunrise, sunset, currentTime);
        todayDetailBinding.moonView.setTimes(moonRise, moonSet, currentTime);
        hasAni = true;
    }

    private void getCurrentTime() {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        float a = Float.parseFloat("+8.0");
        float minute = a * 60;
        now = now.plusMinutes(((int) minute));
        currentTime = now.toString("HH:mm");
    }

}
