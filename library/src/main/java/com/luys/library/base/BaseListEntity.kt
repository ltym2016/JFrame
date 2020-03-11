package com.luys.library.base

/**
 * @author luys
 * @describe
 * @date 2020/3/11
 * @email samluys@foxmail.com
 */
data class BaseListEntity<T>(val total:Int, val list:ArrayList<T>) {
}