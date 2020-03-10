package com.luys.library.http

import io.reactivex.functions.Consumer

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class ExceptionHandle : Consumer<Throwable> {
    override fun accept(t: Throwable) {
        ExceptionManger.handleException(t)
    }
}