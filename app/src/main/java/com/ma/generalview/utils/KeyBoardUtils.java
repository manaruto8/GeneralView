package com.ma.generalview.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class KeyBoardUtils {

    //隐藏虚拟键盘
    public static void hideKeyboard(View v,Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(Window window,Context context) {
        if (window == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            IBinder token = window.getCurrentFocus().getWindowToken();
            if (null == token) return;
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //显示虚拟键盘
    public static void showKeyboard(View v,Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    //强制显示或者关闭系统键盘
    public static void toggleKeyboard(final EditText txtSearchKey, final String status) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 100);
    }

    //通过定时器强制隐藏虚拟键盘
    public static void timerHideKeyboard(final View v, final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, 10);
    }

    //输入法是否显示着
    public static boolean isShow(Context context) {
        boolean bool = false;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            bool = true;
        }
        return bool;

    }
}
