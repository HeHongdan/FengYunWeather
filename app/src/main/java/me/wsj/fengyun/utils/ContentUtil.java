package me.wsj.fengyun.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;

import androidx.preference.PreferenceManager;

import java.lang.reflect.Method;

import me.wsj.fengyun.MyApp;

/**
 * Created by niuchong on 2019/4/7.
 */

public class ContentUtil {

    //应用设置里的文字
//    public static String SYS_LANG = "zh";
    public static String APP_SETTING_UNIT = PreferenceManager.getDefaultSharedPreferences(MyApp.Companion.getContext()).getString("unit", "she");

    public static boolean UNIT_CHANGE = false;
    public static boolean CITY_CHANGE = false;

    public static int visibleHeight = 0;
}
