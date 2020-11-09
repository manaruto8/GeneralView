package com.ma.generalview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.ma.generalview.R;

import java.util.ArrayList;
import java.util.List;

public class VerifyCodeEditText extends LinearLayout implements TextWatcher, View.OnKeyListener,View.OnFocusChangeListener {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";

    private static final String TAG = "VerificationCodeInput";
    private int boxNumber = 4;//验证码输入框数量
    private int boxWidth = 80;
    private int boxHeight = 80;
    private int childHPadding = 14;
    private int childVPadding = 14;
    private String inputType = TYPE_NUMBER;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private Listener listener;
    private boolean focus = false;
    private boolean isCanFocus=true;
    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;
    private Context context;


    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.verifyCodeEditText);
        boxNumber = a.getInt(R.styleable.verifyCodeEditText_box, 4);

        childHPadding = (int) a.getDimension(R.styleable.verifyCodeEditText_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.verifyCodeEditText_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.verifyCodeEditText_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.verifyCodeEditText_box_bg_normal);
        inputType = a.getString(R.styleable.verifyCodeEditText_inputType);
        boxWidth = (int) a.getDimension(R.styleable.verifyCodeEditText_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.verifyCodeEditText_child_height, boxHeight);
        initViews();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();


    }

    private void initViews() {
        for (int i = 0; i < boxNumber; i++) {
            EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;


            editText.setOnKeyListener(this);
            if (i == 0)
                setBg(editText, true);
            else
                setBg(editText, false);
            editText.setTextColor(Color.BLACK);
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);

            }
            editText.setId(i);
            editText.setEms(1);
            editText.addTextChangedListener(this);
            editText.setOnFocusChangeListener(this);
            addView(editText, i);
            mEditTextList.add(editText);

        }


    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    private void focusNext() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < boxNumber; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
            }

        }
    }

    public String getText(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < boxNumber; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }

    public void clearText(){
        for (int i = 0; i < boxNumber; i++) {
            EditText editText = (EditText) getChildAt(i);
            editText.getText().clear();
        }
        isCanFocus=true;
        setEnabled(true);
        mEditTextList.get(0).requestFocus();
        setBg(mEditTextList.get(0), true);
        setBg(mEditTextList.get(boxNumber -1), false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth + childHPadding) * boxNumber - childHPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            focusNext();
            checkAndCommit();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText editText = (EditText) view;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = event.getAction();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                editText.setText("");
                int id=editText.getId();
                if(id>0) {
                    mEditTextList.get(id - 1).requestFocus();
                }
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b&&view instanceof EditText){
            for (int i = 0; i <mEditTextList.size(); i++) {
                EditText editText = mEditTextList.get(i);
                setBg(editText,false);
            }
            EditText editText = (EditText) view;
            setBg(editText,true);
        }
    }

    public interface Listener {
        void onComplete(String content);
    }
}
