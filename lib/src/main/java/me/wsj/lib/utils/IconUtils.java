package me.wsj.lib.utils;


import android.content.Context;

import me.wsj.lib.R;
import per.wsj.commonlib.utils.LogUtil;

public class IconUtils {

    /**
     * 获取白天深色天气图标
     */
    public static int getDayIconDark(Context context, String weather) {
        return getMipmapRes(context, "icon_" + weather + "d", R.mipmap.icon_100d);
    }

    /**
     * 获取白天深色天气图标
     */
    public static int getNightIconDark(Context context, String weather) {
        return getMipmapRes(context, "icon_" + weather + "n", R.mipmap.icon_100n);
    }

    /**
     * 获取白天背景
     */
    public static int getDayBg(Context context, int code) {
        int newCode = ConvertUtil.convert(code);
        return getDrawableRes(context, "bg_" + newCode + "_d", R.drawable.bg_0_d);
    }

    /**
     * 获取晚上背景
     */
    public static int getNightBg(Context context, int code) {
        int newCode = ConvertUtil.convert(code);
        return getDrawableRes(context, "bg_" + newCode + "_n", R.drawable.bg_0_n);
    }

    /**
     * 获取白天背景
     */
    @Deprecated
    public static int getDayBack(Context context, String weather) {
        return getDrawableRes(context, "back_" + weather + "d", R.mipmap.back_100d);
    }

    /**
     * 获取晚上背景
     */
    @Deprecated
    public static int getNightBack(Context context, String weather) {
        return getDrawableRes(context, "back_" + weather + "n", R.mipmap.back_100n);
    }

    public static int getDrawableRes(Context context, String weather, int def) {
        return getRes(context, "drawable", weather, def);
    }

    public static int getMipmapRes(Context context, String weather, int def) {
        return getRes(context, "mipmap", weather, def);
    }

    public static int getRes(Context context, String type, String weather, int def) {
        try {
            int id = context.getResources().getIdentifier(weather, type, context.getPackageName());
            if (id == 0) {
                id = def;
            }
            return id;
        } catch (Exception e) {
            LogUtil.e("获取资源失败：" + weather);
            return def;
        }
    }
}
