package com.luys.jframe

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luys.jframe.databinding.FragmentNetworkBinding
import com.luys.library.base.*
import com.samluys.statusbar.StatusBarUtils
import kotlinx.android.synthetic.main.fragment_network.*


class NetworkFragment : BaseFragment<FragmentNetworkBinding, NetworkViewModel>() {

    private val mList = mutableListOf<BaseBindingItem>()
    private val mAdapter: BaseBindingAdapter by lazy {
        BaseBindingAdapter(object : OnItemClickListener {
            override fun onItemClick(view: View, item: BaseBindingItem) {
                val index = mList.indexOf(item)
                val entity = mList[index]

//            if (entity is HomeListEntity) {
//                with(entity) {
//                    val bundle = Bundle()
//                    bundle.apply {
//                        putInt("job_id", job_id)
//                    }
//                    startContainerActivity(DetailFragment::class.java.canonicalName, bundle)
//                }
//            }

//                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        })
    }

    override fun initParam() {
        super.initParam()
        mToolbarHeight = getToolbarHeight(context,activity)
    }

    override fun getLayoutId(): Int = R.layout.fragment_network

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        mAdapter.setList(mList)
        rv_content.layoutManager = LinearLayoutManager(context)
        rv_content.adapter = mAdapter
        viewModel.getData()
    }

    override fun initViewObservable() {

        viewModel.requestStatus.observe(this, Observer {
            when (it) {
                Constants.REQUEST_OK -> {
                    mList.clear()
                    mList.addAll(viewModel.getList())
                    mAdapter.notifyDataSetChanged()

                    loadingHelper.showLoadingSuccess()
                }
                Constants.REQUEST_FAIL -> {
                    loadingHelper.showLoadingFail()
                }
                Constants.REQUEST_NO_DATA -> {
                    loadingHelper.showEmpty()
                }
                Constants.REQUEST_NO_NET -> {
                    loadingHelper.showNoNet()
                }
                Constants.REQUEST_NO_MORE -> {
                    mList.clear()
                    mList.addAll(viewModel.getList())
                    if (viewModel.getPage() > 1) {
                        mList.add(NoMoreDataEntity())
                    }
                    mAdapter.notifyDataSetChanged()
                    loadingHelper.showLoadingSuccess()
                }
                Constants.REQUEST_DATA_ERROR -> {
                    loadingHelper.showLoadingFail(mContext.resources.getString(R.string.data_error))
                }
            }
        })
    }

    /**
     * 获取标题栏的高度+状态栏的高度
     * @param context
     * @param activity
     * @return
     */
    private fun getToolbarHeight(context: Context?, activity: Activity?): Int {
        return StatusBarUtils.dp2px(context, 44f) + StatusBarUtils.getStatusBarHeight(activity)
    }
}
