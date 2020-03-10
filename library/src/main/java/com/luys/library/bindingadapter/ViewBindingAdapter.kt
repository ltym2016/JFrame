package com.luys.library.bindingadapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding3.view.clicks
import com.luys.library.callback.BindingCommand
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
object ViewBindingAdapter {

    // 防重复点击间隔(秒)
    private const val CLICK_INTERVAL = 1L

    /**
     * 普通View 的点击事件
     *
     * @param view 普票view （Button，TextView...）
     * @param clickCommand 绑定的命令
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("onClickCommand")
    @JvmStatic
    fun onClickCommand(
        view: View,
        clickCommand: BindingCommand<Any>?
    ) {
        view.clicks()
            .throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS) //1秒钟内只允许点击1次
            .subscribe(Consumer<Any?> {
                clickCommand?.execute()
            })
    }

    /**
     * 普通View 的点击事件
     *
     * @param view         普票view （Button，TextView...）
     * @param clickCommand 绑定的命令
     */
    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("onClickScaleCommand")
    fun onClickScaleCommand(
        view: ImageView,
        clickCommand: BindingCommand<Any>?
    ) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).start()
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    clickCommand?.execute()
                }
            }
            true
        }
    }

    /**
     * 动态设置字体颜色
     *
     * @param textView
     * @param colorId
     */
    @BindingAdapter("txtColor")
    fun setTxtColor(textView: TextView, colorId: Int) {
        if (colorId > 0) {
            textView.setTextColor(textView.context.resources.getColor(colorId))
        }
    }

    /**
     * 图片动态加载本地资源
     *
     * @param imageView
     * @param resId
     */
    @BindingAdapter("android:src")
    fun setImageRes(imageView: ImageView, resId: Int) {
        if (resId > 0) {
            imageView.setImageResource(resId)
        }
    }
}