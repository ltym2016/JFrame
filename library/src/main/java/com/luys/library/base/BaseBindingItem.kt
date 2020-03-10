package com.luys.library.base

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
interface BaseBindingItem {
    fun getViewType() : Int
    fun isFooter() : Boolean{
        return false
    }

    fun isHeader() : Boolean {
        return false
    }
}