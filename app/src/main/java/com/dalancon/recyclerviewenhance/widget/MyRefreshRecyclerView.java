package com.dalancon.recyclerviewenhance.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by dalancon on 2019/3/28.
 */

public class MyRefreshRecyclerView extends WrapRecyclerView {

    /**
     * 正常状态
     */
    private int STATUS_AIDL = 0;
    /**
     * 下拉刷新中
     */
    private int STATUS_REFRESHING = 1;

    /**
     * 列表当前的状态
     */
    private int currentStatus = STATUS_AIDL;

    /**
     * 下拉刷新控件的高度
     */
    private int mHeaderViewHight = 0;

    /**
     * 下拉刷新View
     */
    private View mHeaderView = null;


    public MyRefreshRecyclerView(@NotNull Context context) {
        super(context);
    }

    public MyRefreshRecyclerView(@NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshRecyclerView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("TAG", "onLayout");
        super.onLayout(changed, l, t, r, b);

        if (mHeaderView == null) {
            if (getAdapter() instanceof WrapAdapter) {
                WrapAdapter mAdapter = (WrapAdapter) getAdapter();
                mHeaderView = mAdapter.getHeaderView();

                if (mHeaderViewHight == 0) {
                    mHeaderViewHight = mHeaderView.getMeasuredHeight();
                }

                MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                layoutParams.topMargin = -mHeaderViewHight + 1;
                mHeaderView.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                int currentMarginTop = layoutParams.topMargin;
                Log.e("TAG", "currentMarginTop -> $currentMarginTop  mHeaderViewHight -> $mHeaderViewHight");
                if (Math.abs(currentMarginTop) >= mHeaderViewHight / 2) {// 下拉的距离小于头部高度的一般，头部View缩回去
                    currentStatus = STATUS_AIDL;
                    resetHeaderView();
                } else {//自动将头部的View拉出来，进入下拉刷新状态
                    currentStatus = STATUS_REFRESHING;
                    showHeaderView();
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private float downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (canScrollUp()) {
                    return super.onTouchEvent(e);
                }

//                if (currentStatus != STATUS_REFRESHING) return super.onTouchEvent(e)
                scrollToPosition(0);

                float dy = (e.getRawY() - downY) * 0.35f;
                Log.e("TAG", "dy -> $dy  mHeaderViewHight -> $mHeaderViewHight");
                if (dy > 0 && dy > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    int marginTop = 0;
                    if (mHeaderViewHight - dy > 0) {
                        marginTop = (int) (-1 * (mHeaderViewHight - dy));
                    } else {
                        marginTop = 0;
                    }
                    Log.e("TAG", " marginTop -> $marginTop");

                    MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                    layoutParams.topMargin = marginTop;
                    mHeaderView.setLayoutParams(layoutParams);

                    return false;
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    private void showHeaderView() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        int currentMarginTop = layoutParams.topMargin;

        ValueAnimator animator = ObjectAnimator.ofInt(currentMarginTop, 0)
                .setDuration(-currentMarginTop);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                mHeaderView.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }

    private void resetHeaderView() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        int currentMarginTop = layoutParams.topMargin;

        ValueAnimator animator = ObjectAnimator.ofInt(currentMarginTop, -mHeaderViewHight + 1)
                .setDuration(mHeaderViewHight);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                mHeaderView.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }

    private boolean canScrollUp() {
        return canScrollVertically(-1);
    }
}
