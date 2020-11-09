package com.ma.generalview.view

import android.animation.LayoutTransition
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.ma.generalview.R
import kotlinx.android.synthetic.main.item_googlesearch.view.*


class GoogleSearchView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private var onLeftClickListener:OnLeftClickListener?=null
    private var onRight1ClickListener:OnRight1ClickListener?=null
    private var onRight2ClickListener:OnRight2ClickListener?=null
    private var onAccountClickListener:OnAccountClickListener?=null

    private var mTransition: LayoutTransition = LayoutTransition()

    private var leftTag: Int =LEFT_IV_MENU
    private var right1Tag: Int = RIGHT_IV1_SEARCH
    private var right2Tag: Int =RIGHT_IV2_MIC

    companion object {
        const val LEFT_IV_MENU = 0X001
        const val LEFT_IV_ARROW = 0X002

        const val RIGHT_IV1_SEARCH = 0X003

        const val RIGHT_IV2_MIC = 0X003
        const val RIGHT_IV2_CLOSE = 0X004


    }

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.item_googlesearch,this)
        search_left_iv.setOnClickListener {
            if(leftTag == LEFT_IV_MENU) {
                onLeftClickListener?.onLeftClick(leftTag)
            }else if(leftTag == LEFT_IV_ARROW){
                setReset()
            }

        }
        search_right_iv1.setOnClickListener {
            //onRight1ClickListener?.onRight1Click(right1Tag)
            if(right1Tag == RIGHT_IV1_SEARCH) {
                search_right_iv1.visibility= View.GONE
                search_right_iv2.setImageResource(R.drawable.ic_search_close)
                right2Tag= RIGHT_IV2_CLOSE
            }
        }
        search_right_iv2.setOnClickListener {
            if(right2Tag == RIGHT_IV2_MIC) {
                onRight2ClickListener?.onRight2Click(right2Tag)
            }else if(right2Tag == RIGHT_IV2_CLOSE){
                search_text_et.setText("")
            }
        }
        search_right_account.setOnClickListener {
            onAccountClickListener?.onAccountClick()
        }
        search_text_et.setOnEditorActionListener { p0, p1, p2 ->
            if (p1== EditorInfo.IME_ACTION_SEARCH){
                search_right_iv1.visibility= View.VISIBLE
                search_right_iv2.setImageResource(R.drawable.ic_search_mic)
                right2Tag= RIGHT_IV2_MIC

            }
            false
        }
        search_text_et.setOnFocusChangeListener{ _, hasFocus ->
            val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                dp2px(context,50f)
            )

            if (hasFocus) {
                lp.setMargins(0,  dp2px(context,8f), 0,  dp2px(context,8f))
                search_layout.layoutParams = lp
                search_right_account.visibility= View.GONE
                search_left_iv.setImageResource(R.drawable.ic_search_arrowback)
                search_layout.setBackgroundResource(R.drawable.bg_search_layout_bottom_border)
                leftTag= LEFT_IV_ARROW
            } else {
                lp.setMargins(dp2px(context,16f),
                    dp2px(context,8f),
                    dp2px(context,16f),
                    dp2px(context,8f))
                search_layout.layoutParams = lp
                search_right_iv1.visibility= View.GONE
                search_right_account.visibility= View.VISIBLE
                search_left_iv.setImageResource(R.drawable.ic_search_menu)
                search_layout.setBackgroundResource(R.drawable.bg_search_layout_corner_border)
                leftTag= LEFT_IV_MENU
            }

        }
        search_text_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length!! <=0){
                    search_right_iv2.setImageResource(R.drawable.ic_search_mic)
                    right2Tag= RIGHT_IV2_MIC
                }else{
                    search_right_iv2.setImageResource(R.drawable.ic_search_close)
                    right2Tag= RIGHT_IV2_CLOSE
                }
            }

        })
        mTransition.enableTransitionType(LayoutTransition.CHANGING)
        mTransition.setDuration(300)
        mTransition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int) {
                Log.e("abc","start")
                if (view is LinearLayout) {
//                    search_account_iv.visibility= View.VISIBLE
//                    search_menu_iv.setImageResource(R.drawable.ic_search_menu)
                }else{

                }
            }

            override fun endTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int) {
                Log.e("abc","end")
                if (view is LinearLayout) {
//                    search_account_iv.visibility= View.GONE
//                    search_menu_iv.setImageResource(R.drawable.ic_search_arrowback)
                }else{

                }
            }
        })

        search_layout.layoutTransition=mTransition
    }

    private fun setReset(){
//        val lp = LayoutParams(
//            LayoutParams.MATCH_PARENT,
//            dp2px(context,50f)
//        )
//        lp.setMargins(dp2px(context,16f),
//            dp2px(context,8f),
//            dp2px(context,16f),
//            dp2px(context,8f))
//        search_layout.layoutParams = lp
//        search_account_iv.visibility= View.VISIBLE
//        search_menu_iv.setImageResource(R.drawable.ic_search_menu)
//        tag= MENUTAG
        search_text_et.setText("")
        search_text_et.clearFocus()
    }

    /**
     * dp转换成px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转换成dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }


    fun setOnLeftClickListener(onLeftClickListener:OnLeftClickListener){
        this.onLeftClickListener=onLeftClickListener
    }

    fun setOnRight1ClickListener(onRight1ClickListener:OnRight1ClickListener){
        this.onRight1ClickListener=onRight1ClickListener
    }

    fun setOnRight2ClickListener(onRight2ClickListener:OnRight2ClickListener){
        this.onRight2ClickListener=onRight2ClickListener
    }

    fun setOnAccountClickListener(onAccountClickListener:OnAccountClickListener){
        this.onAccountClickListener=onAccountClickListener
    }

    interface OnLeftClickListener{
        fun onLeftClick(tag:Int)
    }

    interface OnRight1ClickListener{
        fun onRight1Click(tag:Int)
    }

    interface OnRight2ClickListener{
        fun onRight2Click(tag:Int)
    }

    interface OnAccountClickListener{
        fun onAccountClick()
    }

    interface OnFocusChangeListener {
        fun onFocusChange(hasFocus: Boolean)
    }

    interface OnQueryTextListener {
        fun onQueryTextChange(newText: CharSequence): Boolean
    }
}