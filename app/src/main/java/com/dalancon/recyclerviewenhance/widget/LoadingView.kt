package com.dalancon.recyclerviewenhance.widget

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout

/**
 * Created by dalancon on 2019/3/29.
 */
class LoadingView : LinearLayout {
    lateinit var leftView: LoadingCircleView
    lateinit var middleView: LoadingCircleView
    lateinit var rightView: LoadingCircleView

    private var colorNormal = Color.parseColor("#AEEEEE")
    private var colorSelected = Color.parseColor("#8EE5EE")

    var index = 0

    var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (index % 3) {
                0 -> {
                    leftView.changeColor(colorSelected)
                    middleView.changeColor(colorNormal)
                    rightView.changeColor(colorNormal)
                }
                1 -> {
                    leftView.changeColor(colorNormal)
                    middleView.changeColor(colorSelected)
                    rightView.changeColor(colorNormal)
                }
                2 -> {
                    leftView.changeColor(colorNormal)
                    middleView.changeColor(colorNormal)
                    rightView.changeColor(colorSelected)
                }
            }
            index++
            sendEmptyMessageDelayed(0, 350)
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        //定义方向
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER

        //添加三个圆点
        leftView = getCircleView(context)
        middleView = getCircleView(context)

        var middelLayoutParams = middleView.layoutParams as LinearLayout.LayoutParams
        middelLayoutParams.leftMargin = dip2px(10f)
        middelLayoutParams.rightMargin = dip2px(10f)
        middleView.layoutParams = middelLayoutParams

        rightView = getCircleView(context)
        Log.e("TAG", "constructor THREE")
        addView(leftView)
        addView(middleView)
        addView(rightView)

        mHandler
    }

    /**
     * 创建加载的小圆点
     */
    private fun getCircleView(context: Context): LoadingCircleView {
        var circleView = LoadingCircleView(context)
        var params = LayoutParams(dip2px(12f), dip2px(12f))
        circleView.layoutParams = params
        return circleView
    }

    private fun dip2px(dip: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics).toInt()
    }

    fun exchangeColor() {
        mHandler.removeMessages(0)
        mHandler.sendEmptyMessage(0)
    }

}