package me.wsj.fengyun;

import android.app.Application;
import android.content.Context;

import dagger.hilt.android.HiltAndroidApp;


@HiltAndroidApp
public class MyApplication extends Application {
    //获取屏幕的高，宽
    private static MyApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取context对象
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }


}
