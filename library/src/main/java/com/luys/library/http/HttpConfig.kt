package com.luys.library.http

import okhttp3.Interceptor

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
object HttpConfig {
    var cacheName:String = "HttpCache"
    var cacheSize = 10 * 1024 * 1024
    var timeout = 15
    var isDebug = false
    var host : String = ""
    var uploadHost : String? = null
    var addHeaderInterceptor : Interceptor? = null
    var getParamsInterceptor : Interceptor? = null
}