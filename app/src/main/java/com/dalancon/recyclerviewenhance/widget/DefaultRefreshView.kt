package com.dalancon.recyclerviewenhance.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dalancon.recyclerviewenhance.R
import kotlinx.android.synthetic.main.header_layout.view.*

/**
 * Created by dalancon on 2019/3/28.
 */
class DefaultRefreshView(var context: Context) : RefreshViewCreator() {
    override fun createRefreshView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.header_layout, parent, false)
    }

    override fun pullRefresh(refreshView: View, dy: Int) {

        if (dy == 0) {
            var scale = 1 - 0.9f * Math.abs(dy) / Math.abs(refreshView.measuredHeight)

            Log.e("DefaultRefreshView", "scale -> $scale")

            var scaleX = ObjectAnimator.ofFloat(refreshView.mTextView, "scaleX", 1f, 1.2f)
            var scaleY = ObjectAnimator.ofFloat(refreshView.mTextView, "scaleY", 1f, 1.2f)
            var animatorSet = AnimatorSet()
            animatorSet.duration = 1000
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.start()
        }
    }

}