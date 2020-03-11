package com.luys.jframe

import android.app.Application
import androidx.databinding.ObservableInt
import com.luys.library.base.BaseViewModel
import com.luys.library.base.SingleLiveEvent
import com.luys.library.http.ExceptionManger
import com.luys.library.http.RetrofitHelper
import io.reactivex.functions.Consumer

class NetworkViewModel(application: Application) : BaseViewModel(application) {

    private var mPage = 1
    private val mTotal: ObservableInt by lazy {
        ObservableInt()
    }
    private val mList = mutableListOf<HomeListEntity>()
    val requestStatus: SingleLiveEvent<Int> by lazy {
        SingleLiveEvent<Int>()
    }

    fun getData() {
        val api = RetrofitHelper.getRetrofitApi(ApiService::class.java).jobList(mPage, 10)
        addSubscribe(RetrofitHelper.subscript(api, Consumer {
            if (it.code == 1) {
                mTotal.set(it.data.total)
                val entityList = it.data.list
                if (entityList.isNotEmpty()) {
                    if (mPage == 1) {
                        mList.clear()
                    }

                    mList.addAll(entityList)

                    if (mTotal.get() == mList.size) {
                        requestStatus.value = Constants.REQUEST_NO_MORE
                    } else {
                        requestStatus.value = Constants.REQUEST_OK
                    }
                } else {
                    if (mPage == 1) {
                        noData()
                    } else {
                        requestStatus.value = Constants.REQUEST_NO_MORE
                    }
                }
            }
        }, Consumer{
            val errCode = ExceptionManger.handleException(it)
            if (mList.isEmpty()) {
                requestStatus.value = Constants.REQUEST_REMIND
            } else {
                when(errCode) {
                    1-> requestStatus.value = Constants.REQUEST_NO_NET
                    2-> requestStatus.value = Constants.REQUEST_FAIL
                    else -> requestStatus.value = Constants.REQUEST_DATA_ERROR
                }
            }
        }))
    }

    private fun noData() {
        requestStatus.value = Constants.REQUEST_NO_DATA
    }

    fun getList(): List<HomeListEntity> {
        return mList
    }

    fun setPage(mPage: Int) {
        this.mPage = mPage
    }

    fun getPage(): Int {
        return mPage
    }
}
