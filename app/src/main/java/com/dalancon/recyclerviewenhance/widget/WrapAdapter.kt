package com.dalancon.recyclerviewenhance.widget

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by dalancon on 2019/3/26.
 */
private const val TYPE_HEADER = 0
private const val TYPE_LIST = 1
private const val TYPE_FOOTER = 2

class WrapAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    private var headerViews = ArrayList<View>()
    private var footerViews = ArrayList<View>()

    fun setHeaderView(view: View) {
        headerViews.add(view)
    }

    fun getHeaderView(): View {
        return headerViews[0]
    }

    fun getFooterView(): View {
        return footerViews[0]
    }

    fun setFooterView(view: View) {
        footerViews.add(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderFooterHolder(headerViews[0])
            TYPE_FOOTER -> HeaderFooterHolder(footerViews[0])
            else -> mAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return headerViews.size + footerViews.size + mAdapter.itemCount
    }


    override fun getItemViewType(position: Int): Int {
        return if (headerViews.size > 0) {
            when {
                position == 0 -> TYPE_HEADER
                position <= mAdapter.itemCount -> TYPE_LIST
                else -> TYPE_FOOTER
            }
        } else {
            when {
                position < mAdapter.itemCount -> TYPE_LIST
                else -> TYPE_FOOTER
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_LIST)
            mAdapter.onBindViewHolder(holder, position - headerViews.size)
    }


    /**
     * 头部底部的holder
     */
    inner class HeaderFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}