package com.luys.library.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luys.library.activity.ContainerActivity
import com.luys.library.view.LoadingDialog
import com.luys.library.view.LoadingView
import com.samluys.jutils.FileUtils
import com.samluys.statusbar.StatusBarUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import java.io.File
import java.lang.reflect.ParameterizedType

/**
 * @author luys
 * @describe
 * @date 2020/3/10
 * @email samluys@foxmail.com
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : RxFragment(), IBaseView {
    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    private var viewModelId = 0
    private var tipDialog: LoadingDialog? = null
    protected lateinit var loadingHelper: LoadingView.LoadingHelper
    protected var mToolbarHeight: Int = 0
    lateinit var mContext: Context
    protected val tempPath: String by lazy {
        FileUtils.getLocalCacheFilePath(Environment.DIRECTORY_PICTURES + File.separator + "temp" + File.separator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        val frameLayout = FrameLayout(inflater.context)
        frameLayout.addView(binding.root)
        loadingHelper = LoadingView.wrap(frameLayout)
            .toobarHeight(mToolbarHeight)
            .withRetry(Runnable {
                onLoadRetry()
            })
        return frameLayout
    }

    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registerLiveDataCallBack()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
    }

    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        val type = javaClass.genericSuperclass
        val modelClass = if (type is ParameterizedType) {
            val typeTemp = type as ParameterizedType
            typeTemp.actualTypeArguments[1] as Class<VM>
        } else {
            BaseViewModel::class.java
        }

        viewModel = createViewModel(this, modelClass) as VM
        binding.setVariable(viewModelId, viewModel)
        lifecycle.addObserver(viewModel)
        viewModel.injectLifecycleProvider(this)
    }

    override fun initParam() { // 黑色字体
        StatusBarUtils.StatusBarIconDark(this.activity)
    }

    private fun registerLiveDataCallBack() {
        viewModel.getLiveData().showDialogEvent.observe(viewLifecycleOwner, Observer {
            showDialog(it)
        })

        viewModel.getLiveData().dismissDialogEvent.observe(viewLifecycleOwner, Observer {
            dismissDialog()
        })

        viewModel.getLiveData().startActivityEvent.observe(viewLifecycleOwner, Observer {
            val clz = it[BaseViewModel.CLASS] as Class<*>
            val bundle = it[BaseViewModel.BUNDLE] as Bundle
            startActivity(clz, bundle)
        })

        viewModel.getLiveData().finishEvent.observe(viewLifecycleOwner, Observer {
            activity?.finish()
        })

        //关闭上一层
        //关闭上一层
        viewModel.getLiveData().onBackPressedEvent
            .observe(viewLifecycleOwner,
                Observer {
                    activity?.onBackPressed() })

        // 跳入ContainerActivity
        // 跳入ContainerActivity
        viewModel.getLiveData().startContainerActivityEvent.observe(
            this,
            Observer { params ->
                val canonicalName =
                    params!![BaseViewModel.CANONICAL_NAME] as String?
                val bundle = params[BaseViewModel.BUNDLE] as Bundle?
                startContainerActivity(canonicalName!!, bundle)
            })

        //跳入Service页面
        viewModel.getLiveData().startServiceEvent.observe(
            viewLifecycleOwner,
            Observer { params ->
                val clz =
                    params!![BaseViewModel.CLASS] as Class<*>?
                val bundle = params[BaseViewModel.BUNDLE] as Bundle?
                startService(clz, bundle)
            })
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(mContext, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(mContext, clz)
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
        val intent = Intent(mContext, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName)
        bundle?.let {
            intent.putExtra(ContainerActivity.BUNDLE, it)
        }
        startActivity(intent)
    }

    /**
     * 跳转Service
     *
     * @param clz    所跳转的目的Service类
     * @param bundle 跳转所携带的信息
     */
    open fun startService(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (isAdded) {
            activity?.startService(intent)
        }
    }

    fun showDialog(title:String) {
        if (tipDialog == null) {
            tipDialog = LoadingDialog(mContext)
        }
        tipDialog!!.showDialog(title)
    }

    fun dismissDialog() {
        tipDialog?.dismiss()
    }

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    open fun initViewModel(): VM? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        binding.unbind()
    }

    private fun <T : ViewModel> createViewModel(fragment: Fragment, cls: Class<T>): T {
        return ViewModelProvider(fragment)[cls]
    }

    protected open fun onLoadRetry() {
    }

    fun isBackPressed():Boolean = false
}