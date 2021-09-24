package me.wsj.lib.utils

import android.content.Context
import me.wsj.lib.R
import me.wsj.lib.utils.DateUtil.getNowHour
import me.wsj.lib.utils.WeatherUtil.convert
import per.wsj.commonlib.utils.LogUtil

object IconUtils {
    /**
     * 获取白天深色天气图标
     */
    @JvmStatic
    fun getDayIconDark(context: Context, weather: String): Int {
        val code = parseCode(weather)
        return getMipmapRes(context, "icon_" + code + "d", R.mipmap.icon_100d)
    }

    private fun parseCode(weather: String): Int {
        var weather = weather
        if (weather.isEmpty()) {
            weather = "0"
        }
        var code = weather.toInt()
        if (code in 201 until 300) {
            code = 200
        }
        if (code == 154 || code == 153) {
            code = 101
        }
        return code
    }

    /**
     * 获取白天深色天气图标
     */
    @JvmStatic
    fun getNightIconDark(context: Context, weather: String): Int {
        val code = parseCode(weather)
        return getMipmapRes(context, "icon_" + code + "n", R.mipmap.icon_100n)
    }

    /**
     * 获取白天背景
     */
    val defaultBg: Int
        get() = if (isDay()) R.drawable.bg_0_d else R.drawable.bg_0_n

    /**
     * 判断是否为白天。
     *
     * @return 是否白天。
     */
    @JvmStatic
    fun isDay():Boolean{
        val now = getNowHour()
        return now in 7..18
    }

    /**
     * 获取(白天/黑夜)背景。
     *
     * @param context 上下文。
     * @param code 天气的代号。
     * @return Int 背景的资源Id。
     */
    fun getBg(context: Context, code: Int): Int {
        return if (isDay()) getDayBg(context, code)
        else getNightBg(context, code)
    }

    /**
     * 获取白天背景
     */
    fun getDayBg(context: Context, code: Int): Int {
        var newCode = convert(code)
        if (newCode > 10) {
            newCode = newCode / 10
        }
        return getDrawableRes(context, "bg_" + newCode + "_d", R.drawable.bg_0_d)
    }

    /**
     * 获取晚上背景
     */
    fun getNightBg(context: Context, code: Int): Int {
        var newCode = convert(code)
        if (newCode > 10) {
            newCode = newCode / 10
        }
        return getDrawableRes(context, "bg_" + newCode + "_n", R.drawable.bg_0_n)
    }

    fun getDrawableRes(context: Context, weather: String, def: Int): Int {
        return getRes(context, "drawable", weather, def)
    }

    fun getMipmapRes(context: Context, weather: String, def: Int): Int {
        return getRes(context, "mipmap", weather, def)
    }

    fun getRes(context: Context, type: String?, weather: String, def: Int): Int {
        return try {
            var id = context.resources.getIdentifier(weather, type, context.packageName)
            if (id == 0) {
                id = def
            }
            id
        } catch (e: Exception) {
            LogUtil.e("获取资源失败：$weather")
            def
        }
    }
}