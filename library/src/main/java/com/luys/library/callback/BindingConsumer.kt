package com.luys.library.callback

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
interface BindingConsumer<T> {
    fun call(t:T)
}