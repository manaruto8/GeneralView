package com.ma.generalview.utils;

import android.util.Log;

import java.util.List;

public class LogUtils {

    public static final boolean openLog = true;
    public static final String mTag = "app";

    /**
     * 输出debug log
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (openLog && tag != null && msg != null) {
            if(tag.isEmpty()){
                tag= mTag;
            }
            Log.d(tag, msg);
        }
    }

    /**
     * 输出错误 log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (openLog && tag != null && msg != null) {
            if(tag.isEmpty()){
                tag= mTag;
            }
            Log.e(tag, msg);
        }
    }


    public static void e(String msg) {
        if (openLog && msg != null) {
            Log.d(mTag, msg);
        }
    }

    /**
     * 输出警告 log
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (openLog && tag != null && msg != null) {
            if(tag.isEmpty()){
                tag= mTag;
            }
            Log.w(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (openLog && tag != null && msg != null) {
            if(tag.isEmpty()){
                tag= mTag;
            }
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (openLog && tag != null && msg != null) {
            if(tag.isEmpty()){
                tag= mTag;
            }
            Log.i(tag, msg);
        }
    }


    public static <T> void printList(List<T> list) {
        if(openLog) {
            if (list == null || list.size() < 1) {
                i(mTag,"---list数量为0---");
                return;
            }
            int size = list.size();
            i(mTag,"---begin---");
            for (int i = 0; i < size; i++) {
                i(mTag,i + ":" + list.get(i).toString());
            }
            i(mTag,"---end---");
        }
    }

    public static <T> void printArray(T[] array) {
        if(openLog) {
            if (array == null || array.length < 1) {
                i(mTag,"---array数量为0---");
                return;
            }
            int length = array.length;
            i(mTag,"---begin---");
            for (int i = 0; i < length; i++) {
                i(mTag,i + ":" + array[i].toString());
            }
            i(mTag,"---end---");
        }
    }
}
