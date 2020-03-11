package com.luys.library.base

/**
 * @author luys
 * @describe
 * @date 2020/3/11
 * @email samluys@foxmail.com
 */
data class BaseResponse<T>(val code:Int = 0,val msg:String, val time:String, val data: T) {

}