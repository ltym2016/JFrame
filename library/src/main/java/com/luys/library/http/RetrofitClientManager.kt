package com.luys.library.http

import com.google.gson.Gson
import com.samluys.jutils.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
open class RetrofitClientManager {
    private var mOkHttpClient: OkHttpClient? = null

    init {
        initOkHttpClient()
    }


    private fun initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized(RetrofitClientManager::class.java) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    val cache = Cache(File(Utils.getContext().externalCacheDir, HttpConfig.cacheName),
                        HttpConfig.cacheSize.toLong())
                    val builder = OkHttpClient.Builder()
                        .cache(cache)
                        .retryOnConnectionFailure(true)
                        .connectTimeout(HttpConfig.timeout.toLong(), TimeUnit.SECONDS)
                        .writeTimeout(HttpConfig.timeout.toLong(), TimeUnit.SECONDS)
                        .readTimeout(HttpConfig.timeout.toLong(), TimeUnit.SECONDS)

                    HttpConfig.addHeaderInterceptor?.let {
                        // 添加请求头
                        builder.addInterceptor(it)
                    }

                    HttpConfig.getParamsInterceptor?.let {
                        // 获取请求参数的拦截器
                        builder.addInterceptor(it)
                    }

                    if (HttpConfig.isDebug) {
                        // 设置log格式
                        builder.addInterceptor(InterceptorUtils.logInterceptor())
                    }

                    builder.addInterceptor(ResponseInterceptor())
                    mOkHttpClient = builder.build()
                }
            }
        }
    }

    fun <T> getRetrofit(url:String, clazz : Class<T>) : T {
        val builder = Retrofit.Builder()
            .baseUrl(url)
            .client(mOkHttpClient!!)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        return builder.build().create(clazz)
    }


}