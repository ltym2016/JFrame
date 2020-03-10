package com.luys.library.http

import com.samluys.jutils.log.LogUtils
import okhttp3.Interceptor
import okhttp3.ResponseBody

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class InterceptorUtils {

    companion object{
        fun logInterceptor() : Interceptor {
            return Interceptor {
                val request = it.request()
                val response = it.proceed(request)
                val body = response.body()
                val mediaType = body?.contentType()
                val content = body?.string()?:""
                LogUtils.e("request url====>" + request.url())
                LogUtils.json("response result", content)
                response.newBuilder().body(ResponseBody.create(mediaType,content)).build()
            }
        }
    }

}