package me.wsj.fengyun.utils;


import android.content.Context;

import me.wsj.fengyun.R;
import per.wsj.commonlib.utils.LogUtil;

public class IconUtils {

    /**
     * 获取白天深色天气图标
     */
    public static int getDayIconDark(Context context, String weather) {
        return getRes(context, "icon_" + weather + "d", R.mipmap.icon_100d);
    }

    /**
     * 获取白天深色天气图标
     */
    public static int getNightIconDark(Context context, String weather) {
        return getRes(context, "icon_" + weather + "n", R.mipmap.icon_100n);
    }

    /**
     * 获取白天背景
     */
    public static int getDayBack(Context context, String weather) {
        return getRes(context, "back_" + weather + "d", R.mipmap.back_100d);
    }

    /**
     * 获取晚上背景
     */
    public static int getNightBack(Context context, String weather) {
        return getRes(context, "back_" + weather + "n", R.mipmap.back_100n);
    }

    public static int getRes(Context context, String weather, int def) {
        try {
            int id = context.getResources().getIdentifier(weather, "mipmap", context.getPackageName());
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
