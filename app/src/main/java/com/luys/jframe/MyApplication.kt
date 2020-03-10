package com.luys.jframe

import android.app.Application
import com.luys.library.http.HttpUtils
import com.samluys.jutils.Utils
import com.samluys.jutils.log.LogUtils

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
        LogUtils.newBuilder()
            .debug(Utils.isDebug())
            .tag("JFRAME_LOG")
            .build()

        HttpUtils.init(Utils.getStringFromConfig(R.string.host))
            .isDebug(Utils.isDebug())
    }
}