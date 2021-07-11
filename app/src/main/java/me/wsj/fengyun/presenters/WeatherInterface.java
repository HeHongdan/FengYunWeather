package me.wsj.fengyun.presenters;


import com.qweather.sdk.bean.WarningBean;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;

/**
 * Created by niuchong on 2018/5/17.
 */

public interface WeatherInterface {

    /**
     * 空气实况
     */
//    void getAirNow(AirNowBean bean);

    /**
     * 空气预报
     */
//    void getAirForecast(AirForecast bean);


    /**
     * 逐小时预报
     */
    void getWeatherHourly(WeatherHourlyBean bean);

}
