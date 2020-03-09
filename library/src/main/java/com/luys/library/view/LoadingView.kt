package com.luys.library.view

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.*
import com.luys.library.R
import com.samluys.jutils.NetworkUtils
import com.samluys.jutils.log.LogUtils

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
object LoadingView {

    /**
     * 加载中
     */
    const val STATUS_LOADING = 1
    /**
     * 加载成功
     */
    const val STATUS_LOAD_SUCCESS = 2
    /**
     * 加载失败
     */
    const val STATUS_LOAD_FAILED = 3
    /**
     * 加载为空
     */
    const val STATUS_EMPTY_DATA = 4

    /**
     * 没有网路
     */
    const val STATUS_NO_NET = 5


    fun wrap(activity: Activity): LoadingHelper {
        val viewGroup = activity.findViewById<ViewGroup>(R.id.content)
        val loadingView: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_loading, null)
        return LoadingHelper(activity, loadingView, viewGroup)
    }

    fun wrap(viewGroup: ViewGroup): LoadingHelper {
        val loadingView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.layout_loading, viewGroup, false)
        return LoadingHelper(
            viewGroup.context,
            loadingView,
            viewGroup
        )
    }

    class LoadingHelper(
        private val context: Context,
        private val mLoadingView: View,
        private val mViewGroup: ViewGroup
    ) {
        private var mCurState = 0
        private var mToolbarHeight = 0
        private var mRetryTask: Runnable? = null

        fun withRetry(task: Runnable?): LoadingHelper {
            mRetryTask = task
            return this
        }

        fun setBackground(colorId: Int) {
            mLoadingView.setBackgroundColor(context.resources.getColor(colorId))
        }

        /**
         * ================显示加载中=================
         */
        fun showLoading() {
            showLoadingStatus(STATUS_LOADING, null, 0)
        }

        fun showLoading(msg: String) {
            showLoadingStatus(STATUS_LOADING, msg, 0)
        }

        fun showLoading(imageId: Int) {
            showLoadingStatus(STATUS_LOADING, null, imageId)
        }

        fun showLoading(msg: String, imageId: Int) {
            showLoadingStatus(STATUS_LOADING, msg, imageId)
        }

        /**
         * ================加载成功=================
         */
        fun showLoadingSuccess() {
            showLoadingStatus(STATUS_LOAD_SUCCESS, null, 0)
        }

        /**
         * ================加载失败=================
         */
        fun showLoadingFail() {
            showLoadingStatus(STATUS_LOAD_FAILED, null, 0)
        }

        fun showLoadingFail(msg: String) {
            showLoadingStatus(STATUS_LOAD_FAILED, msg, 0)
        }

        fun showLoadingFail(imageId: Int) {
            showLoadingStatus(STATUS_LOAD_FAILED, null, imageId)
        }

        fun showLoadingFail(msg: String, imageId: Int) {
            showLoadingStatus(STATUS_LOAD_FAILED, msg, imageId)
        }

        /**
         * ================空数据=================
         */
        fun showEmpty(msg: String, imageId: Int) {
            showLoadingStatus(STATUS_EMPTY_DATA, msg, imageId)
        }

        fun showEmpty(imageId: Int) {
            showLoadingStatus(STATUS_EMPTY_DATA, null, imageId)
        }

        fun showEmpty(msg: String?) {
            showLoadingStatus(STATUS_EMPTY_DATA, msg, 0)
        }

        fun showEmpty() {
            val noData = context.resources.getString(R.string.no_data)
            showLoadingStatus(STATUS_EMPTY_DATA, noData, 0)
        }

        /**
         * ================空数据=================
         */
        fun showNoNet(msg: String, imageId: Int) {
            showLoadingStatus(STATUS_NO_NET, msg, imageId)
        }

        fun showNoNet(imageId: Int) {
            showLoadingStatus(STATUS_NO_NET, null, imageId)
        }

        fun showNoNet(msg: String) {
            showLoadingStatus(STATUS_NO_NET, msg, 0)
        }

        fun showNoNet() {
            showLoadingStatus(STATUS_NO_NET, null, 0)
        }

        private fun showLoadingStatus(statusParam: Int, msg: String?, imageId: Int) {
            var status = statusParam
            if (!NetworkUtils.isConnected()) {
                LogUtils.e("网络不可用")
                status = STATUS_NO_NET
            }
            if (mCurState == status) {
                return
            }
            mCurState = status
            try {
                val iv_loading =
                    mLoadingView.findViewById<ImageView>(R.id.iv_loading)
                // 加载动画
                val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.load_progress_animation
                )
                // 使用ImageView显示动画
                val lin = LinearInterpolator() //设置动画匀速运动
                hyperspaceJumpAnimation.interpolator = lin
                iv_loading.startAnimation(hyperspaceJumpAnimation)
                val ll_loading_fail =
                    mLoadingView.findViewById<LinearLayout>(R.id.ll_loading_fail)
                val btn_tryAgain =
                    mLoadingView.findViewById<Button>(R.id.btn_tryAgain)
                val tv_text = mLoadingView.findViewById<TextView>(R.id.tv_text)
                val iv_status_icon =
                    mLoadingView.findViewById<ImageView>(R.id.iv_status_icon)
                val ll_empty = mLoadingView.findViewById<LinearLayout>(R.id.ll_empty)
                val tv_empty_text =
                    mLoadingView.findViewById<TextView>(R.id.tv_empty_text)
                val iv_empty =
                    mLoadingView.findViewById<ImageView>(R.id.iv_empty)
                val btn_refresh =
                    mLoadingView.findViewById<Button>(R.id.btn_refresh)
                // 设置图片
                mViewGroup.removeView(mLoadingView)
                mLoadingView.isClickable = true
                mViewGroup.addView(mLoadingView)
                val lp =
                    mLoadingView.layoutParams as FrameLayout.LayoutParams
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                lp.topMargin = mToolbarHeight
                if (mViewGroup.indexOfChild(mLoadingView) != mViewGroup.childCount - 1) {
                    mLoadingView.bringToFront()
                }
                if (status == STATUS_LOAD_SUCCESS) {
                    mLoadingView.visibility = View.GONE
                } else {
                    mLoadingView.visibility = View.VISIBLE
                }
                when (status) {
                    STATUS_LOAD_FAILED -> { // 服务器异常
                        iv_loading.visibility = View.GONE
                        ll_loading_fail.visibility = View.VISIBLE
                        if (!TextUtils.isEmpty(msg)) {
                            tv_text.text = setSpanText(
                                context.resources.getString(
                                    R.string.loading_fail_msg,
                                    msg
                                )
                            )
                        } else {
                            tv_text.text =
                                setSpanText(context.resources.getString(R.string.loading_fail))
                        }
                        iv_status_icon.setImageResource(R.drawable.ic_loading_fail)
                        btn_tryAgain.setOnClickListener {
                            if (mRetryTask != null) {
                                mRetryTask!!.run()
                            }
                        }
                    }
                    STATUS_NO_NET -> { // 网络异常
                        iv_loading.visibility = View.GONE
                        ll_loading_fail.visibility = View.VISIBLE
                        tv_text.text = setSpanText(context.resources.getString(R.string.no_net))
                        iv_status_icon.setImageResource(R.drawable.ic_no_net)
                        btn_tryAgain.setOnClickListener {
                            if (mRetryTask != null) {
                                mRetryTask!!.run()
                            }
                        }
                    }
                    STATUS_EMPTY_DATA -> {
                        iv_loading.visibility = View.GONE
                        ll_loading_fail.visibility = View.GONE
                        ll_empty.visibility = View.VISIBLE
                        iv_empty.visibility = View.VISIBLE
                        if (imageId != 0) {
                            iv_empty.setImageResource(imageId)
                        }
                        tv_empty_text.text = setSpanText(msg)
                        btn_refresh.setOnClickListener {
                            if (mRetryTask != null) {
                                mRetryTask!!.run()
                            }
                        }
                    }
                    else -> {
                        ll_loading_fail.visibility = View.GONE
                        ll_empty.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun toobarHeight(height: Int): LoadingHelper {
            mToolbarHeight = height
            return this
        }

        private fun setSpanText(text: String?): SpannableString {
            if (TextUtils.isEmpty(text)) {
                return SpannableString("")
            } else if (!text!!.contains("\n")) {
                return SpannableString(text)
            }
            val spannableString = SpannableString(text)
            val endIndex = text.indexOf("\n")
            // 颜色#333333
            val userNameColor =
                ForegroundColorSpan(context.resources.getColor(R.color.color_333333))
            spannableString.setSpan(userNameColor, 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            // 粗体
            val span = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(span, 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }

    }
}