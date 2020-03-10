package com.luys.library.http

import android.accounts.NetworkErrorException
import com.google.gson.JsonSyntaxException
import com.samluys.jutils.ToastUtils
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class ExceptionManger {

    companion object{
        fun handleException(e:Throwable) : Int {
            var errCode = 0
            var errMsg = ""
            if (e is ConnectException
                || e is TimeoutException
                || e is NetworkErrorException
                || e is UnknownHostException
            ) {
                errMsg = "网络连接异常"
                errCode = 1
            } else if (e is JsonSyntaxException
                || e is NullPointerException
            ) {
                errMsg = "数据解析异常"
            } else {
                if (e is HttpException) {
                    val httpException = e as HttpException
                    if (httpException.code() == 500) {
                        errMsg = "服务器异常 code : " + 500
                        errCode = 2
                    } else {


                    }
                } else {
                    errMsg = "加载数据失败，请稍后再试"
                    e.printStackTrace()
                }

                if (errMsg.isNotEmpty()) {
                    ToastUtils.showShort(errMsg)
                }
            }
            return errCode
        }
    }

}