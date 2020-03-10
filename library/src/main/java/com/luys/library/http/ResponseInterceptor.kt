package com.luys.library.http

import android.text.TextUtils
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class ResponseInterceptor : ResponseBodyInterceptor() {

    override fun intercept(response: Response, url: String?, body: String?): Response {

        var jsonObject: JSONObject? = null

        body?.let {
            jsonObject = JSONObject(it)
            jsonObject?.apply {
                val data = getString("data")
                // 这里对接口可能返回的data类型，无数据的情况做统一处理
                if (TextUtils.isEmpty(data) || "[]" == data || "{}" == data) {
                    put("data", null)
                }
            }
        }

        if (response.body() != null && jsonObject != null) {
            val contentType = response.body()!!.contentType()
            val responseBody = ResponseBody.create(contentType, jsonObject.toString())
            return response.newBuilder().body(responseBody).build()
        }

        return response
    }
}