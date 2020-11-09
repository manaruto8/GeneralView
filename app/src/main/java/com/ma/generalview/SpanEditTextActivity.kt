package com.ma.generalview

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.ma.generalview.view.SpanEditText
import kotlinx.android.synthetic.main.activity_span_edit_text.*

class SpanEditTextActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_span_edit_text)
        initView()
    }

    private fun initView() {
        btn_span_test1.setOnClickListener(this)
        btn_span_test2.setOnClickListener(this)
        btn_span_test3.setOnClickListener(this)
        et_span_content.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                SpanEditText.KeyDownHelper(et_span_content.text)
            } else false
        })
        val s="请点击[测试]按钮测试"
        et_span_content.setText(et_span_content.subSpanString(s))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_span_test1 -> {
                et_span_content.addSpan("[测试]")
            }
            R.id.btn_span_test2 -> {
                et_span_content.addSpan("[测试]")
            }
            R.id.btn_span_test3 -> {
                et_span_content.addSpan("[测试]")
            }
        }
    }
}