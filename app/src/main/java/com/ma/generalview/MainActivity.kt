package com.ma.generalview

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ma.generalview.utils.ToastUtils
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
        main_verifyCodeInput.setOnClickListener(this)
        main_spanInput.setOnClickListener(this)
        main_st.setOnClickListener(this)
        main_lt.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.main_radarchat -> {
                val toRadarChat = Intent(this, RadarChartActivity::class.java)
                startActivity(toRadarChat)
            }
            R.id.main_googlesearch -> {
                val toGoogleSearch = Intent(this, GoogleSearchActivity::class.java)
                startActivity(toGoogleSearch)
            }
            R.id.main_verifyCodeInput -> {
                val toVerifyCode = Intent(this, VerifyCodeActivity::class.java)
                startActivity(toVerifyCode)
            }
            R.id.main_spanInput -> {
                val toSpan = Intent(this, SpanEditTextActivity::class.java)
                startActivity(toSpan)
            }
            R.id.main_st -> {
                ToastUtils.showShort(this,"1")
            }
            R.id.main_lt -> {
                ToastUtils.showLong(this,"2")
            }
        }
    }
}