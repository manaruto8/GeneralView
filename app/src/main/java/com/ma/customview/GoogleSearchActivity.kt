package com.ma.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Gravity
import com.ma.customview.view.GoogleSearchView
import kotlinx.android.synthetic.main.activity_google_search.*

import java.lang.Exception

class GoogleSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_search)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolBar)
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        googleSearchView.setOnLeftClickListener(object : GoogleSearchView.OnLeftClickListener{
            override fun onLeftClick(tag :Int) {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        })
        googleSearchView.setOnRight1ClickListener(object :GoogleSearchView.OnRight1ClickListener{
            override fun onRight1Click(tag :Int) {
                val pm = packageManager;
                val activities = pm.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
                if (activities.size != 0) {//触发事件
                    startVoiceRecognitionActivity()
                }
            }
        })
        googleSearchView.setOnRight2ClickListener(object :GoogleSearchView.OnRight2ClickListener{
            override fun onRight2Click(tag :Int) {
                val pm = packageManager;
                val activities = pm.queryIntentActivities(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
                if (activities.size != 0) {//触发事件
                    startVoiceRecognitionActivity()
                }
            }
        })
        googleSearchView.setOnAccountClickListener(object :GoogleSearchView.OnAccountClickListener{
            override fun onAccountClick() {

            }
        })
    }


    fun startVoiceRecognitionActivity() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请对着麦克风说话！");
            startActivityForResult(intent, 0X001);
        } catch ( e:Exception) {
            e.printStackTrace();
        }
    }

}