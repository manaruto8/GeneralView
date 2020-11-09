package com.ma.generalview.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static final boolean showToast = true;
    private static Toast mToast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        show(context,message,Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        show(context,message,Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        show(context,message,Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        show(context,message,Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (showToast&&context!=null&&message!=null){
            if (mToast==null) {
                mToast=Toast.makeText(context, message, duration);
            }else {
                mToast.setText(message);
                mToast.setDuration(duration);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (showToast&&context!=null){
            if (mToast==null) {
                mToast=Toast.makeText(context, message, duration);
            }else {
                mToast.setText(message);
                mToast.setDuration(duration);
            }
            mToast.show();
        }
    }


}