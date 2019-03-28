package com.dalancon.recyclerviewenhance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.dalancon.recyclerviewenhance.widget.DefaultRefreshView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setRefreshCreator(DefaultRefreshView(this))

        var dataAdapter = ItemAdapter(this)

        mRecyclerView.adapter = dataAdapter

        mRecyclerView.postDelayed(object : Runnable {
            override fun run() {
                mRecyclerView.stopRefresh()
            }

        }, 5000)

    }
}
