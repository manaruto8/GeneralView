package com.ma.customview

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        main_radarchat.setOnClickListener(this)
        main_googlesearch.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.main_radarchat -> {
                val toRadarChat = Intent(this, RadarChartActivity::class.java)
                startActivity(toRadarChat)
            }
            R.id.main_googlesearch -> {
                val toGoogleSearch = Intent(this, GoogleSearchActivity::class.java)
                startActivity(toGoogleSearch)
            }
        }
    }
}