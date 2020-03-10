package com.luys.library.http

import okhttp3.Interceptor

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class HttpUtils {

    companion object{
        fun init(host:String) : Builder = Builder(host)
    }

    class Builder(private val host:String) {

        init {
            HttpConfig.host = host
        }

        /**
         * 缓存存放的文件名称
         * @param cacheName
         * @return
         */
        fun cacheName(cacheName:String) : Builder {
            HttpConfig.cacheName = cacheName
            return this
        }

        /**
         * 缓存存放的文件最大size
         * @param cacheSize
         * @return
         */
        fun cacheSize(cacheSize: Int): Builder {
            HttpConfig.cacheSize = cacheSize
            return this
        }

        /**
         * 请求超时
         * @param timeout
         * @return
         */
        fun timeout(timeout: Int): Builder {
            HttpConfig.timeout = timeout
            return this
        }

        /**
         * 是否为debug模式
         * @param isDebug
         * @return
         */
        fun isDebug(isDebug: Boolean): Builder {
            HttpConfig.isDebug = isDebug
            return this
        }

        /**
         * 添加头文件请求拦截器
         * @param interceptor
         * @return
         */
        fun addHeaderInterceptor(interceptor: Interceptor): Builder {
            HttpConfig.addHeaderInterceptor = interceptor
            return this
        }

        /**
         * 获取请求参数的拦截器
         * @param interceptor
         * @return
         */
        fun getParamsInterceptor(interceptor: Interceptor): Builder {
            HttpConfig.getParamsInterceptor = interceptor
            return this
        }
    }


}