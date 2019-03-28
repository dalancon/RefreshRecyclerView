package com.dalancon.recyclerviewenhance.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**自定义头部和底部View
 * Created by dalancon on 2019/3/26.
 */

open class WrapRecyclerView : RecyclerView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    private var wrapAdapter: WrapAdapter = WrapAdapter()


    /**
     * 设置headerView
     */
    fun setHeaderView(headerView: View) {
        wrapAdapter.setHeaderView(headerView)
    }

    /**
     * 设置headerView
     */
    fun setFooterView(footerView: View) {
        wrapAdapter.setFooterView(footerView)
    }

    override fun setAdapter(adapter: Adapter<RecyclerView.ViewHolder>?) {
        wrapAdapter.mAdapter = adapter!!
        super.setAdapter(wrapAdapter)

    }


}
