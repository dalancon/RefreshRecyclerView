package com.dalancon.recyclerviewenhance.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 加载的小圆点
 * Created by dalancon on 2019/3/29.
 */
class LoadingCircleView : View {

    //半径
    private var mRadius = 0f

    private lateinit var mPaint: Paint

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.parseColor("#8EE5EE")
        //#AEEEEE
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(Math.min(width, height), Math.min(width, height))
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (changed) {
            mRadius = (measuredHeight / 2).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!.drawCircle(mRadius, mRadius, mRadius, mPaint)
    }

    fun changeColor(color: Int) {
        mPaint.color = color
        invalidate()
    }

}