package com.ma.customview


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ma.customview.view.VerifyCodeEditText
import kotlinx.android.synthetic.main.activity_verify_code.*

class VerifyCodeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)
        verifyCodeEditText.setOnCompleteListener {

        }
    }
}