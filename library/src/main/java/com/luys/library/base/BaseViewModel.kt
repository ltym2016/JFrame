package com.luys.library.base

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.luys.library.R
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * @author luys
 * @describe
 * @date 2020/3/6
 * @email samluys@foxmail.com
 */
class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    private val mLiveData: ViewLiveData<Any> by lazy {
        ViewLiveData<Any>()
    }
    lateinit var lifecycle:WeakReference<LifecycleProvider<Any>> //弱引用持有
    lateinit var mCompositeDisposable : CompositeDisposable // 管理RxJava，主要针对RxJava异步操作造成的内存泄漏

    /**
     * 注入RxLifecycle生命周期
     */
    fun injectLifecycleProvider(lifecycle:LifecycleProvider<Any>) {
        this.lifecycle = WeakReference<LifecycleProvider<Any>>(lifecycle)
    }

    /**
     * 设置标题内容
     */
    var titleName: ObservableField<String> = ObservableField()
    /**
     * 设置标题内容字体颜色
     */
    var titleColor: ObservableField<Int> = ObservableField(R.color.color_title)
    /**
     * 设置标题右侧文字内容
     */
    var rightName: ObservableField<String> = ObservableField()
    /**
     * 设置标题右侧Button内容
     */
    var rightButtonName: ObservableField<String> = ObservableField()
    /**
     * 设置标题右侧图片资源
     */
    var rightImage: ObservableField<Int> = ObservableField()
    /**
     * 设置标题左侧图片资源
     */
    var leftImage: ObservableField<Int> = ObservableField(R.drawable.ic_left_arrow)
    /**
     * 设置标题栏下方的分割线是否显示 默认显示
     */
    var divider: ObservableBoolean = ObservableBoolean(true)

    fun getLifecycleProvider():LifecycleProvider<Any>? = lifecycle.get()

    fun getLiveData() = mLiveData

    fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    class ViewLiveData<T> : SingleLiveEvent<T>(){
        val showDialogEvent: SingleLiveEvent<String> = SingleLiveEvent<String>()
        val dismissDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val startActivityEvent: SingleLiveEvent<Map<String, Any>> = SingleLiveEvent<Map<String, Any>>()
        val startContainerActivityEvent: SingleLiveEvent<Map<String, Any>>? = SingleLiveEvent<Map<String, Any>>()
        val startContainerNoSlideActivityEvent: SingleLiveEvent<Map<String, Any>> = SingleLiveEvent<Map<String, Any>>()
        var finishEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val onBackPressedEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val showCommonDialog: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val showGiftDialog: SingleLiveEvent<String> = SingleLiveEvent<String>()
        val startServiceEvent: SingleLiveEvent<Map<String, Any>> = SingleLiveEvent<Map<String, Any>>()
        val startPrivateChatEvent: SingleLiveEvent<Map<String, String>> = SingleLiveEvent<Map<String, String>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
        }
    }
}