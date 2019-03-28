package com.dalancon.recyclerviewenhance

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * Created by dalancon on 2019/3/26.
 */

class ItemAdapter(var mContext: Context) : RecyclerView.Adapter<ItemAdapter.MyHolder>() {

    private var layoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyHolder {
        return MyHolder(layoutInflater.inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.mTextView.text = "Item$position"
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
