package com.luys.library.callback

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
interface BindingFunction<T> {
    fun call() : T
}