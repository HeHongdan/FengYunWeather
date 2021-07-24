package me.wsj.fengyun.ui.base;

import android.content.Intent;

import androidx.viewbinding.ViewBinding;

/**
 * Created by shiju.wang on 2018/2/10.
 */

public interface CreateInit<T extends ViewBinding> {

    T bindView();

    /**
     * 接收数据
     * @param intent
     */
    void prepareData(Intent intent);

    /**
     * 初始化布局组件
     */
    void initView();

    /**
     * 处理事件
     */
    void initEvent();

    /**
     * 初始化数据
     */
    void initData();

}
