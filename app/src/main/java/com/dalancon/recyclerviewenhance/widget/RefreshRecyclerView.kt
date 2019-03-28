package com.dalancon.recyclerviewenhance.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by dalancon on 2019/3/26.
 */
class RefreshRecyclerView : WrapRecyclerView {

    private var mRefreshViewCreator: RefreshViewCreator? = null

    /**
     * 正常状态
     */
    private val STATUS_AIDL = 0
    /**
     * 下拉刷新中
     */
    private val STATUS_REFRESHING = 1

    /**
     * 列表当前的状态
     */
    private var currentStatus = STATUS_AIDL

    /**
     * 下拉刷新控件的高度
     */
    private var mHeaderViewHight = 0

    /**
     * 下拉刷新View
     */
    private var mHeaderView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    /**
     * 设置头部刷新的View
     */
    fun setRefreshCreator(refreshViewCreator: RefreshViewCreator) {
        this.mRefreshViewCreator = refreshViewCreator
        setHeaderView(this.mRefreshViewCreator!!.createRefreshView(this))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.e("TAG", "onLayout")
        super.onLayout(changed, l, t, r, b)

        if (mHeaderView == null) {
            if (adapter is WrapAdapter) {
                var mAdapter = adapter as WrapAdapter
                mHeaderView = mAdapter.getHeaderView()

                if (mHeaderViewHight == 0) {
                    mHeaderViewHight = mHeaderView!!.measuredHeight
                }

                var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
                layoutParams.topMargin = -mHeaderViewHight + 1
                mHeaderView!!.layoutParams = layoutParams
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_UP -> {
                var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
                var currentMarginTop = layoutParams.topMargin
                Log.e("TAG", "currentMarginTop -> $currentMarginTop  mHeaderViewHight -> $mHeaderViewHight")
                if (currentMarginTop < 0) {// 下拉的距离小于头部高度的一般，头部View缩回去
                    currentStatus = STATUS_AIDL
                    resetHeaderView()
                } else {
                    mRefreshViewShow = true
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    fun stopRefresh() {
        mRefreshViewShow = false
        currentStatus = STATUS_AIDL
        resetHeaderView()
    }

    /**
     * 重置headerView
     */
    private fun resetHeaderView() {
        var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
        var currentMarginTop = layoutParams.topMargin
        Log.e("TAG", " currentMarginTop -> ${currentMarginTop}  finalValue -> ${(-mHeaderViewHight + 1).toFloat()}")

        val animator = ObjectAnimator.ofInt(currentMarginTop, -mHeaderViewHight + 1)
                .setDuration(mHeaderViewHight.toLong())

        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
                layoutParams.topMargin = animation!!.animatedValue as Int
                mHeaderView!!.layoutParams = layoutParams
            }
        })
        animator.start()
    }

    /**
     * 重置headerView
     */
    private fun showHeaderView() {
        var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
        var currentMarginTop = layoutParams.topMargin
        Log.e("TAG", "currentMarginTop ==== $currentMarginTop")
        val animator = ObjectAnimator.ofInt(currentMarginTop, 0)
                .setDuration(10)

        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                Log.e("TAG", "onAnimationUpdate==== ${animation!!.animatedValue}")
                var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
                layoutParams.topMargin = animation!!.animatedValue as Int
                mHeaderView!!.layoutParams = layoutParams
            }
        })
        animator.start()
    }

    private var downY = 0f

    private var mRefreshViewShow = false

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        Log.e("TAG", " onTouchEvent -> ${e!!.action}")
        when (e!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = e.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (canScrollUp() || mRefreshViewShow) {
                    return super.onTouchEvent(e)
                }


                scrollToPosition(0)

                var dy = (e.rawY - downY) * 0.35f
                if (dy > 0 && dy > ViewConfiguration.get(context).getScaledTouchSlop()) {
                    var marginTop = if (mHeaderViewHight - dy > 0) {
                        -1 * (mHeaderViewHight - dy).toInt()
                    } else {
                        0
                    }

                    var layoutParams = mHeaderView!!.layoutParams as MarginLayoutParams
                    layoutParams.topMargin = marginTop
                    mHeaderView!!.layoutParams = layoutParams
                    mRefreshViewCreator!!.pullRefresh(mHeaderView!!, marginTop)

                    return false
                }
            }
        }

        return super.onTouchEvent(e)
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    private fun canScrollUp(): Boolean {
        return canScrollVertically(-1)
    }

}