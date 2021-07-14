package me.wsj.fengyun.utils;


import androidx.preference.PreferenceManager;

import me.wsj.fengyun.MyApplication;

/**
 * Created by niuchong on 2019/4/7.
 */

public class ContentUtil {

    //应用设置里的文字
//    public static String SYS_LANG = "zh";
    public static String APP_SETTING_UNIT = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getString("unit","she");

    public static String APP_SETTING_THEME = "浅色";


    public static boolean UNIT_CHANGE = false;
    public static boolean CITY_CHANGE = false;

}
