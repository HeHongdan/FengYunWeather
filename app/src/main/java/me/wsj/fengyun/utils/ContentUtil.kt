package me.wsj.fengyun.utils

import androidx.preference.PreferenceManager
import me.wsj.fengyun.MyApp.Companion.context
import me.wsj.fengyun.MyApp

/**
 * Created by niuchong on 2019/4/7.
 */
object ContentUtil {
    //应用设置里的文字
    //    public static String SYS_LANG = "zh";
    /** 单位的值(摄氏度/华氏度)。 */
    @JvmField
    var APP_SETTING_UNIT =
        PreferenceManager.getDefaultSharedPreferences(context).getString("unit", "she")
    /** 单位(摄氏度/华氏度)。 */
    @JvmField
    var UNIT_CHANGE = false
    /** 城市是否改变。 */
    @JvmField
    var CITY_CHANGE = false
    @JvmField
    var visibleHeight = 0
}