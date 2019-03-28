package com.dalancon.recyclerviewenhance.widget

import android.view.View
import android.view.ViewGroup

/**
 * Created by dalancon on 2019/3/28.
 */
abstract class RefreshViewCreator {
    abstract fun createRefreshView(parent: ViewGroup): View

    abstract fun pullRefresh(refreshView: View, dy: Int)
}