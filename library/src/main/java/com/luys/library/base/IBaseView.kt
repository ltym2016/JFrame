package com.luys.library.base

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
interface IBaseView {
    /**
     * 初始化界面传递参数
     */
    fun initParam()
    /**
     * 初始化数据
     */
    fun initData()
    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()
}