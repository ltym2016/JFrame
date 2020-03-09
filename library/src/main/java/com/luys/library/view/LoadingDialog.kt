package com.luys.library.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.luys.library.R
import com.samluys.statusbar.StatusBarUtils
import kotlinx.android.synthetic.main.loading_dialog_layout.*

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
class LoadingDialog(context:Context,themeResId:Int = R.style.loadingDialog) : Dialog(context, themeResId) {

    private var mContext:Context
    private lateinit var tipView:TextView
    init {
        setCanceledOnTouchOutside(false)
        initDialogWidth()
        mContext = context
    }

    private fun initDialogWidth() {
        window?.apply {
            val wmlp = attributes
            wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT
            attributes = wmlp
        }
        setContentView(R.layout.loading_dialog_layout)
        val loadingView = CustomLoadingView(mContext).apply {
            setColor(Color.WHITE)
            setSize(StatusBarUtils.dp2px(mContext,32f))
            val loadingViewLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams = loadingViewLP
        }
        contentWrap.addView(loadingView)
    }

    fun showDialog(tipWord:String) {
        if (tipWord.isNotEmpty()) {
            tipView = TextView(mContext)

            val tipViewLP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tipViewLP.topMargin = StatusBarUtils.dp2px(mContext, 12f)
            tipView.apply {
                tag = 1
                layoutParams = tipViewLP
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER
                maxLines = 2
                setTextColor(ContextCompat.getColor(mContext, android.R.color.white))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                text = tipWord
            }

            if (contentWrap != null && contentWrap.findViewWithTag<View?>(1) == null) {
                contentWrap.addView(tipView)
            }
            show()
        }
    }

    class Builder(private val mContext: Context) {
        private var mTipWord: CharSequence? = null
        /**
         * 设置显示的文案
         */
        fun setTipWord(tipWord: CharSequence): Builder {
            mTipWord = tipWord
            return this
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 [Dialog.show] 方法
         *
         * @param cancelable 按系统返回键是否可以取消
         * @return 创建的 Dialog
         */
        @JvmOverloads
        fun create(cancelable: Boolean = true): LoadingDialog {
            val dialog = LoadingDialog(mContext)
            dialog.setCancelable(cancelable)
            dialog.setContentView(R.layout.loading_dialog_layout)
            val contentWrap =
                dialog.findViewById<View>(R.id.contentWrap) as ViewGroup
            val loadingView = CustomLoadingView(mContext)
            loadingView.setColor(Color.WHITE)
            loadingView.setSize(StatusBarUtils.dp2px(mContext, 32f))
            val loadingViewLP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            loadingView.layoutParams = loadingViewLP
            contentWrap.addView(loadingView)

            mTipWord?.let {
                val tipView = TextView(mContext)
                val tipViewLP = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tipViewLP.topMargin = StatusBarUtils.dp2px(mContext, 12f)
                tipView.layoutParams = tipViewLP
                tipView.ellipsize = TextUtils.TruncateAt.END
                tipView.gravity = Gravity.CENTER
                tipView.maxLines = 2
                tipView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white))
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                tipView.text = it
                contentWrap.addView(tipView)
            }
            return dialog
        }

    }
}