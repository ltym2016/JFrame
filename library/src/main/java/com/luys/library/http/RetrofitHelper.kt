package com.luys.library.http

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class RetrofitHelper : RetrofitClientManager(){

    companion object{
        /**
         * 设置动态host的请求
         * @param url
         * @param clazz
         * @param <T>
         * @return
         */
        fun <T> getRetrofitApi(url:String, clazz:Class<T>) : T {
            return RetrofitHelper().getRetrofit(url,clazz)
        }

        /**
         * 设置固定host的请求
         * @param clazz
         * @param <T>
         * @return
         */
        fun <T> getRetrofitApi(clazz:Class<T>) : T {
            return RetrofitHelper().getRetrofit(HttpConfig.host,clazz)
        }

        fun <T> subscript(observable: Observable<T>, action: Consumer<T>) : Disposable {
            return subscript(observable, Consumer {

            }, action, ExceptionHandle(), Action {

            })
        }

        fun <T> subscript(observable: Observable<T>, action: Consumer<T>, e:Consumer<Throwable>) : Disposable {
            return subscript(observable, Consumer {

            }, action, e, Action {

            })
        }

        fun <T> subscript(observable: Observable<T>, start: Consumer<Disposable>, action: Consumer<T>, complete: Action) : Disposable {
            return subscript(observable,start,action,ExceptionHandle(),complete)
        }


        private fun <T> subscript(
            observable: Observable<T>,
            start: Consumer<Disposable>,
            action: Consumer<T>,
            e: Consumer<Throwable>,
            complete: Action
        ): Disposable {
            return observable
                .compose(RxUtils.iOThreadScheduler())
                .doOnSubscribe(start)
                .subscribe(action, e, complete)
        }
    }
}