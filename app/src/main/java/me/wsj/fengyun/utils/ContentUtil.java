package me.wsj.fengyun.utils;


import androidx.preference.PreferenceManager;

import me.wsj.fengyun.MyApplication;

/**
 * Created by niuchong on 2019/4/7.
 */

public class ContentUtil {

    //用户id
//    public static final String PUBLIC_ID = "HE2106221833431392";
//    //用户key
//    public static final String APK_KEY = "24caabf195034fe6a8cfeb65fdd78bf0";
    //当前所在位置
    public static Double NOW_LON = 116.40;
    public static Double NOW_LAT = 39.9;

    //当前城市
    public static String NOW_CITY_ID = SpUtils.getString(MyApplication.getContext(), "lastLocation", "CN101010100");
    public static String NOW_CITY_NAME = SpUtils.getString(MyApplication.getContext(), "nowCityName", "北京");

    public static boolean FIRST_OPEN = SpUtils.getBoolean(MyApplication.getContext(), "first_open", true);

    //应用设置里的文字
//    public static String SYS_LANG = "zh";
    public static String APP_SETTING_UNIT = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getString("unit","she");

    public static String APP_SETTING_THEME = SpUtils.getString(MyApplication.getContext(), "theme", "浅色");


    public static boolean UNIT_CHANGE = false;
    public static boolean CITY_CHANGE = false;

}
