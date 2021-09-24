package me.wsj.fengyun.ui.base

import android.content.Intent
import androidx.viewbinding.ViewBinding

/**
 * 类描述：创建后初始化。
 *
 * [BaseActivity.init]
 * prepareData()-》initView()-》initEvent()-》initData()
 *
 * @author HeHongdan
 * @date 2021/8/21
 * @since v2021/8/21
 *
 * Created by shiju.wang on 2018/2/10.
 */
interface CreateInit<T : ViewBinding?> {

    /**
     * 绑定视图。
     *
     * @return ViewBinding。
     */
    fun bindView(): T

    /**
     * 准备好(接收)数据。
     *
     * @param intent 意图。
     */
    fun prepareData(intent: Intent?)

    /**
     * 初始化布局组件。
     */
    fun initView()

    /**
     * 初始化事件。
     */
    fun initEvent()

    /**
     * 初始化数据。
     */
    fun initData()
}