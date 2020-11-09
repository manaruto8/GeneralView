package com.ma.generalview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {

    private static final String FILE_NAME = "app";

    public static void write(Context context, String k, int v) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(k, v);
        editor.apply();
    }

    public static void write(Context context,  String k, boolean v) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putBoolean(k, v);
        editor.apply();
    }

    public static void write(Context context, String k, String v) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putString(k, v);
        editor.apply();
    }

    public static int readInt(Context context, String k) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getInt(k, 0);
    }

    public static int readInt(Context context, String k, int defv) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getInt(k, defv);
    }

    public static boolean readBoolean(Context context, String k) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getBoolean(k, false);
    }

    public static boolean readBoolean(Context context, String k, boolean defv) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getBoolean(k, defv);
    }

    public static String readString(Context context, String k) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getString(k, null);
    }

    public static String readString(Context context, String k, String defV) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return preference.getString(k, defV);
    }

    public static void remove(Context context, String k) {
        SharedPreferences preference = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.remove(k);
        editor.apply();
    }

    public static void clean(Context cxt, String fileName) {
        SharedPreferences preference = cxt.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.clear();
        editor.apply();
    }
}
