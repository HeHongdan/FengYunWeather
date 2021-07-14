package me.wsj.bg;


import android.content.Context;

import per.wsj.commonlib.utils.LogUtil;

public class IconUtils {

    /**
     * 获取白天背景
     */
    public static int getDayBg(Context context, int code) {
        int newCode = ConvertUtil.convert(code);
        return getRes(context, "bg_" + newCode + "_d", R.drawable.bg_0_d);
    }

    /**
     * 获取晚上背景
     */
    public static int getNightBack(Context context, String weather) {
        return getRes(context, "bg_" + weather + "_n", R.drawable.bg_0_n);
    }

    public static int getRes(Context context, String weather, int def) {
        try {
            int id = context.getResources().getIdentifier(weather, "drawable", context.getPackageName());
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
