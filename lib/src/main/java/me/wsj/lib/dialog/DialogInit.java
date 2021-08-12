package me.wsj.lib.dialog;

/**
 * Created by shiju.wang on 2018/2/10.
 */

public interface DialogInit {

    /**
     * 子类设置layout
     * @return
     */
    int getLayout();

    /**
     * 初始化布局组件
     */
    void initView();

    /**
     * 增加按钮点击事件
     */
    void initListener();

    /**
     * 初始化数据
     */
    void initData();

}
