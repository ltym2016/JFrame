package com.luys.library.view

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.luys.library.R
import com.samluys.statusbar.StatusBarUtils

/**
 * @author luys
 * @describe
 * @date 2020/3/9
 * @email samluys@foxmail.com
 */
class CustomLoadingView(context: Context, attrs: AttributeSet? = null, defStyleAttr:Int = R.style.Loading)
    : View(context, attrs, defStyleAttr){

    private var mSize = 0
    private var mPaintColor = 0
    private var mAnimateValue = 0
    private var mAnimator: ValueAnimator? = null
    private lateinit var mPaint: Paint

    companion object{
        private val LINE_COUNT = 12
        private val DEGREE_PER_LINE = 360 / LINE_COUNT
    }

    init {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomLoadingView,
            defStyleAttr,
            0
        ).apply {

            mSize = getDimensionPixelSize(R.styleable.CustomLoadingView_loading_view_size,
                StatusBarUtils.dp2px(context, 32f))
            mPaintColor = getInt(R.styleable.CustomLoadingView_android_color, Color.WHITE)
            recycle()

            initPaint();
        }
    }

    constructor(context: Context, size: Int, color: Int) : this(context) {
        mSize = size
        mPaintColor = color
        initPaint()
    }

    private fun initPaint(){
        mPaint = Paint()
        mPaint.color = mPaintColor
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    fun setColor(color: Int) {
        mPaintColor = color
        mPaint.color = color
        invalidate()
    }

    fun setSize(size: Int) {
        mSize = size
        requestLayout()
    }

    fun start() {
        mAnimator?.let {
            if (!it.isStarted) {
                it.start()
            }
        } ?: initAnimator()
    }

    fun stop() {
        mAnimator?.apply {
            removeUpdateListener(mUpdateListener)
            removeAllUpdateListeners()
            cancel()
        }
    }

    private fun initAnimator() {
        mAnimator = ValueAnimator.ofInt(0, CustomLoadingView.LINE_COUNT - 1)
        mAnimator?.apply {
            addUpdateListener(mUpdateListener)
            duration = 600
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    private val mUpdateListener =
        AnimatorUpdateListener { animation ->
            mAnimateValue = animation.animatedValue as Int
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mSize,mSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val saveCount = canvas.saveLayer(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                null,
                Canvas.ALL_SAVE_FLAG
            )
            drawLoading(canvas, mAnimateValue * DEGREE_PER_LINE)
            canvas.restoreToCount(saveCount)
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            start()
        } else {
            stop()
        }
    }

    private fun drawLoading(canvas: Canvas, rotateDegrees: Int) {
        val width = mSize / 12
        val height = mSize / 6
        mPaint.strokeWidth = width.toFloat()
        canvas.rotate(rotateDegrees.toFloat(), mSize / 2.toFloat(), mSize / 2.toFloat())
        canvas.translate(mSize / 2.toFloat(), mSize / 2.toFloat())
        for (i in 0 until LINE_COUNT) {
            canvas.rotate(DEGREE_PER_LINE.toFloat())
            mPaint.alpha = (255f * (i + 1) / LINE_COUNT).toInt()
            canvas.translate(0f, -mSize / 2 + width / 2.toFloat())
            canvas.drawLine(0f, 0f, 0f, height.toFloat(), mPaint)
            canvas.translate(0f, mSize / 2 - width / 2.toFloat())
        }
    }
}