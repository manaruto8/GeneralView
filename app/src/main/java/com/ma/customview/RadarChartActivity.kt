package com.ma.customview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ma.customview.view.RadarChartView
import kotlinx.android.synthetic.main.activity_radar_chart.*
import java.util.*

class RadarChartActivity : BaseActivity() {

    private val list1: MutableList<Int> = ArrayList()
    private val list2: MutableList<Int> = ArrayList()
    private val list3: MutableList<Int> = ArrayList()
    private val textList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radar_chart)
        initData()
        initView()
    }

    private fun initData() {
        for (i in 0..4) {
            list1.add(Random().nextInt(100))
            list2.add(Random().nextInt(100))
            list3.add(Random().nextInt(100))
            textList.add("字体$i")
        }
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        radarChatView.setRadarChartData("#D32F2F",list1);
        radarChatView.setRadarChartData("#388E3C",list2);
        radarChatView.setRadarChartData("#311B92",list3);
    }
}