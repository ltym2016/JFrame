package com.luys.library.base

import android.app.Application
import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.luys.library.R
import com.luys.library.callback.BindingAction
import com.luys.library.callback.BindingCommand
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author luys
 * @describe
 * @date 2020/3/6
 * @email samluys@foxmail.com
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    private val mLiveData: ViewLiveData<Any> by lazy {
        ViewLiveData<Any>()
    }
    private lateinit var lifecycle: WeakReference<LifecycleProvider<*>> //弱引用持有
    private lateinit var mCompositeDisposable: CompositeDisposable // 管理RxJava，主要针对RxJava异步操作造成的内存泄漏

    /**
     * 注入RxLifecycle生命周期
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference<LifecycleProvider<*>>(lifecycle)
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

    fun getLifecycleProvider(): LifecycleProvider<*>? = lifecycle.get()

    fun getLiveData() = mLiveData

    fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * 设置标题右侧点击事件
     */
    val rightClick: BindingCommand<Any> = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            // 设置标题右侧点击事件
            clickRight()
        }
    })

    /**
     * 点击左上角返回按钮
     */
    val backClick:BindingCommand<Any> = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            clickBack()
        }
    })

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

    class ViewLiveData<T> : SingleLiveEvent<T>() {
        val showDialogEvent: SingleLiveEvent<String> = SingleLiveEvent<String>()
        val dismissDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val startActivityEvent: SingleLiveEvent<Map<String, Any>> =
            SingleLiveEvent<Map<String, Any>>()
        val startContainerActivityEvent: SingleLiveEvent<Map<String, Any>> =
            SingleLiveEvent<Map<String, Any>>()
        val startContainerNoSlideActivityEvent: SingleLiveEvent<Map<String, Any>> =
            SingleLiveEvent<Map<String, Any>>()
        var finishEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val onBackPressedEvent: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val showCommonDialog: SingleLiveEvent<Any> = SingleLiveEvent<Any>()
        val showGiftDialog: SingleLiveEvent<String> = SingleLiveEvent<String>()
        val startServiceEvent: SingleLiveEvent<Map<String, Any>> =
            SingleLiveEvent<Map<String, Any>>()
        val startPrivateChatEvent: SingleLiveEvent<Map<String, String>> =
            SingleLiveEvent<Map<String, String>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
        }
    }

    companion object {
        const val CLASS = "CLASS"
        const val CANONICAL_NAME = "CANONICAL_NAME"
        const val BUNDLE = "BUNDLE"
    }

    /**
     * 设置标题右侧点击事件
     */
    protected open fun clickRight() {

    }

    /**
     * 设置标题右侧点击事件
     */
    protected open fun click2Right() {

    }

    /**
     * 设置标题左侧点击事件
     */
    protected open fun clickBack() {
        finish()
    }

    /**
     * 关闭界面
     */
    open fun finish() {
        mLiveData.finishEvent.call()
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(clz, null)
    }

    /**
     * 跳转到Service
     *
     * @param clz    所跳转的目的Service类
     * @param bundle 跳转所携带的信息
     */
    fun startService(clz: Class<*>, bundle: Bundle?) {
        val params: MutableMap<String, Any> =
            HashMap()
        params[CLASS] = clz
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        mLiveData.startServiceEvent.postValue(params)
    }

    /**
     * 跳转到私聊页面
     *
     * @param params  需要传递的参数
     */
    fun startPrivateChat(params: Map<String, String>) {
        mLiveData.startPrivateChatEvent.postValue(params)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params: MutableMap<String, Any> =
            HashMap()
        params[CLASS] = clz
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        mLiveData.startActivityEvent.postValue(params)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName, null)
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val params: MutableMap<String, Any> =
            HashMap()
        params[CANONICAL_NAME] = canonicalName
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        mLiveData.startContainerActivityEvent.postValue(params)
    }

    /**
     * 跳转没有侧滑容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    fun startContainerNoSlideActivity(canonicalName: String) {
        startContainerNoSlideActivity(canonicalName, null)
    }

    /**
     * 跳转没有侧滑容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    fun startContainerNoSlideActivity(
        canonicalName: String,
        bundle: Bundle?
    ) {
        val params: MutableMap<String, Any> =
            HashMap()
        params[CANONICAL_NAME] = canonicalName
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        mLiveData.startContainerNoSlideActivityEvent.postValue(params)
    }

    fun showLoadingDialog() {
        showLoadingDialog("请稍后...")
    }

    fun showLoadingDialog(title: String?) {
        mLiveData.showDialogEvent.postValue(title)
    }

    fun dismissLoadingDialog() {
        mLiveData.dismissDialogEvent.call()
    }
}