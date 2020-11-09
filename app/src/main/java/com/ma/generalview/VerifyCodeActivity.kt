package com.ma.generalview


import android.os.Bundle
import kotlinx.android.synthetic.main.activity_verify_code.*

class VerifyCodeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)
        verifyCodeEditText.setOnCompleteListener {

        }
    }
}