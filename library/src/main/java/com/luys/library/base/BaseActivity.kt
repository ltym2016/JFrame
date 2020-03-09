package com.luys.library.base

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luys.library.activity.ContainerActivity
import com.luys.library.view.LoadingDialog
import com.luys.library.view.LoadingView
import com.samluys.statusbar.StatusBarUtils
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType

/**
 * @author luys
 * @describe
 * @date 2020/3/6
 * @email samluys@foxmail.com
 */
open abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : RxAppCompatActivity(),IBaseView {

    protected lateinit var binding:V
    protected lateinit var viewModel: VM
    private var viewModelId = 0
    private var tipDialog : LoadingDialog? = null
    protected lateinit var loadingHelper : LoadingView.LoadingHelper
    protected var mToolbarHeight:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.transparencyBar(this, false)

        //页面接受的参数方法
        initParam()
        //初始化Databinding和ViewModel方法
        initViewDataBinding()
        //ViewModel与View的契约事件回调逻辑
        doViewLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val rootView = findViewById<FrameLayout>(R.id.content)
        loadingHelper = LoadingView
            .wrap(rootView)
            .toobarHeight(mToolbarHeight)
            .withRetry(Runnable {
                onLoadRetry()
            })
    }

    /**
     * 初始化参数
     */
    override fun initParam() {
        StatusBarUtils.StatusBarIconDark(this)
        setOrientation()
    }

    private fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        viewModelId = initVariableId()

        val type = javaClass.genericSuperclass
        val modelClass = if (type is ParameterizedType) {
            val typeTemp = type as ParameterizedType
            typeTemp.actualTypeArguments[1] as Class<VM>
        } else {
            BaseViewModel::class.java
        }

        viewModel = createViewModel(this, modelClass) as VM
        binding.setVariable(viewModelId,viewModel)
        lifecycle.addObserver(viewModel)
        viewModel.injectLifecycleProvider(this)
    }

    private fun doViewLiveDataCallBack() {
        //加载对话框显示
        viewModel.getLiveData().showDialogEvent
            .observe(this, Observer {
            showDialog(it)
        })
        //加载对话框消失
        viewModel.getLiveData().dismissDialogEvent
            .observe(this, Observer {
                dismissDialog()
        })

        //跳入新页面
        viewModel.getLiveData().startActivityEvent
            .observe(this,
            Observer<Map<String, Any>> {
                val clz = it[BaseViewModel.CLASS] as Class<*>
                val bundle = it[BaseViewModel.BUNDLE] as Bundle
                startActivity(clz, bundle)
            })

        //关闭界面
        viewModel.getLiveData().finishEvent
            .observe(this, Observer {
                finish() })

        //关闭上一层
        viewModel.getLiveData().onBackPressedEvent
            .observe(this, Observer {
                onBackPressed()
            })

        //跳入ContainerActivity
        viewModel.getLiveData().startContainerActivityEvent.observe(
            this,
            Observer {
                val canonicalName = it[BaseViewModel.CANONICAL_NAME] as String
                val bundle = it[BaseViewModel.BUNDLE] as Bundle
                startContainerActivity(canonicalName, bundle)
            })
    }


    fun showDialog(title:String) {
        if (tipDialog == null) {
            tipDialog = LoadingDialog(this)
        }
        tipDialog!!.showDialog(title)
    }

    fun dismissDialog() {
        tipDialog?.dismiss()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        bundle?.let {
            intent.putExtras(it)
        }
        startActivity(intent)
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
    open fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val intent = Intent(this, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName)
        bundle?.let {
            intent.putExtra(ContainerActivity.BUNDLE, it)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        binding.unbind()
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    protected abstract fun setOrientation()

    override fun initData() {}

    override fun initViewObservable() {}

    private fun <T : ViewModel> createViewModel(activity :FragmentActivity, cls:Class<T>) : T {
        return ViewModelProvider(this)[cls]
    }


    protected open fun onLoadRetry() { // override this method in subclass to do retry task
    }
}