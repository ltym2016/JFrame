package com.luys.library.http

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class RxUtils {
    companion object{
        fun <T> iOThreadScheduler(): ObservableTransformer<T, T>? {
            return ObservableTransformer { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
            }
        }

        fun <T> newThreadScheduler(): ObservableTransformer<T, T>? {
            return ObservableTransformer { upstream ->
                upstream
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
            }
        }

        fun <T> mainThreadScheduler(): ObservableTransformer<T, T>? {
            return ObservableTransformer { upstream ->
                upstream
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(AndroidSchedulers.mainThread())
            }
        }
    }
}