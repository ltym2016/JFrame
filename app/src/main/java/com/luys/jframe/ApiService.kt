package com.luys.jframe

import com.luys.library.base.BaseListEntity
import com.luys.library.base.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author luys
 * @describe
 * @date 2020/3/11
 * @email samluys@foxmail.com
 */
interface ApiService {
    @FormUrlEncoded
    @POST("partjob/index")
    fun jobList(@Field("page") page:Int, @Field("pagenum") pagenum:Int) :
            Observable<BaseResponse<BaseListEntity<HomeListEntity>>>

}