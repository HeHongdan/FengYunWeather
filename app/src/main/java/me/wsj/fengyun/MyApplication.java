package me.wsj.fengyun;

import android.app.Application;
import android.content.Context;

import dagger.hilt.android.HiltAndroidApp;
import me.wsj.fengyun.utils.ContentUtil;
import com.qweather.sdk.view.HeConfig;


import java.util.logging.Logger;


@HiltAndroidApp
public class MyApplication extends Application {
    //获取屏幕的高，宽
    private static MyApplication instance = null;

    public Logger log;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //在主线程中new的handler就是主线程的handler
        //初始化Handler
        HeConfig.init(ContentUtil.PUBLIC_ID, ContentUtil.APK_KEY);
        HeConfig.switchToDevService();

    }

    /**
     * 获取context对象
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }


}
