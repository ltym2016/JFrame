package com.luys.library.http

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.nio.charset.StandardCharsets

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
abstract class ResponseBodyInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val response = chain.proceed(request)
        val responseBody = response.body()

        responseBody?.let {
            val contentLength = it.contentLength()
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer
            if ("gzip" == response.headers()["Content-Encoding"]) {
                val gzippedResponseBody = GzipSource(buffer.clone())
                buffer = Buffer()
                buffer.writeAll(gzippedResponseBody)
            }

            val contentType = responseBody.contentType()
            val charset = if (contentType?.charset(StandardCharsets.UTF_8) == null) {
                    StandardCharsets.UTF_8
                } else {
                    contentType.charset(StandardCharsets.UTF_8)!!
                }

            if (charset != null && contentLength != 0L) {
                return intercept(response, url, buffer.clone().readString(charset))
            }
        }

        return response

    }

    abstract fun intercept(
        response: Response,
        url: String?,
        body: String?
    ): Response
}