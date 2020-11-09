package com.ma.generalview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value == null || "".equalsIgnoreCase(value.trim()) || "null".equalsIgnoreCase(value.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判空处理工具
     */
    public static boolean notNull(Object obj) {
        if ("null".equals(obj) || "".equals(obj) || null == obj || "" == obj) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验字符串, 如果如果为空, 则赋值"", 不是则返回字符串
     *
     * @param str
     * @return
     */
    public static String checkString(String str) {
        return isEmpty(str) ? "" : str;
    }

    /**
     * 只要有一个为空, 就返回true
     *
     * @param args
     * @return
     */
    public static boolean isEmpty(String... args) {
        for (String str : args) {
            if (StringUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean equals(String... args) {
        String last = null;
        for (String str : args) {
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equals(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color, int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    public static Spanned getHtmlStyleString(int resId, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>").append(context.getString(resId)).append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    private static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
        }
        return size;
    }

    /**
     * 将流转换成字符串
     *
     * @param file
     */
    public static String streamToString(File file) {
        String content = "";
        if (null == file) return content;
        try {
            int i = 0;
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((i = fis.read()) != -1) {
                bos.write(i);
            }
            content = bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 将bitmap转换成字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        String content = "";
        if (null == bitmap || bitmap.isRecycled()) return content;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        content = Base64.encodeToString(bytes, Base64.DEFAULT);
        return content;
    }

    /**
     * 对字符串进行中文乱码处理
     *
     * @param str
     * @return
     */
    public static String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                formart = str;
            }
        } catch (UnsupportedEncodingException e) {
        }
        return formart;
    }

    /**
     * 设置控件文本部分字体颜色
     *
     * @param text
     * @param color
     * @param startIndex
     * @param length
     * @return
     */
    public static SpannableStringBuilder setTextColor(String text, String color, int startIndex, int length) {
        if (isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, startIndex + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //颜色
        return spannableStringBuilder;
    }

    /**
     * 设置控件文本部分字体颜色(多个颜色设置)
     *
     * @param text
     * @param colorNums  有几种颜色
     * @param colors
     * @param startIndex
     * @param length
     * @return
     */
    public static SpannableStringBuilder setTextColor(String text, int colorNums, String[] colors, int[] startIndex, int[] length) {
        if (isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        for (int i = 0; i < colorNums; i++) {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])), startIndex[i], startIndex[i] + length[i], Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //颜色

        }
        return spannableStringBuilder;
    }

    /**
     * 设置控件Text部分字体大小
     *
     * @param text
     * @param dp         字体大小
     * @param startIndex
     * @param length
     * @return
     */
    public static SpannableStringBuilder setTextSize(String text, int dp, int startIndex, int length,Context context) {
        if (isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(dp2px(context,dp)), startIndex, startIndex + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //字号
        return spannableStringBuilder;
    }

    /**
     * @param text
     * @param size            字体大小(dp)
     * @param sizeStartIndex  要设置的字在字符串中的索引位置
     * @param sizelength      长度
     * @param color
     * @param colorStartIndex
     * @param colorLength
     * @return
     */
    public static SpannableStringBuilder setTextSizeAndColor(String text, int size, int sizeStartIndex, int sizelength, String color, int colorStartIndex, int colorLength,Context context) {
        if (isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(dp2px(context,size)), sizeStartIndex, sizeStartIndex + sizelength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //字号
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), colorStartIndex, colorStartIndex + colorLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //颜色
        return spannableStringBuilder;
    }


    /**
     * 过滤出来文本中所有的数字
     *
     * @param number
     * @return
     */
    public static String filterNumber(String number) {
        number = number.replaceAll("[^(0-9)]", "");
        return number;
    }

    /**
     * 截取过长文本以...结尾
     *
     * @param text
     * @param end
     * @return
     */
    public static String subStringEndDot(String text, int end) {
        text = checkString(text);
        if (text.length() > end) {
            text = text.substring(0, end) + "...";
        }
        return text;
    }

    /**
     * 截取字符串
     *
     * @param start
     * @param end
     * @return
     */
    public static String subString(int start, int end, String str) {
        if (str.length() < end) {
            return str;
        }
        return str.substring(start, end);
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param phone
     * @return
     */
    public static String hidePhone(String phone) {
        if (StringUtils.isEmpty(phone)
                || phone.length() != 11) {
            return null;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 隐藏姓名后面
     *
     * @param name
     * @return
     */
    public static String hideName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (name.length() == 1) {
            return name;
        } else {
            String nameHeader = name.substring(0, 1);
            String nameFooter = name.substring(1, name.length());
            if (nameFooter.length() == 1) {
                return appendStr(nameHeader, "*");
            } else {
                return appendStr(nameHeader, "**");
            }
        }
    }

    /**
     * 字符串的拼接
     *
     * @param args
     * @return
     */
    public static String appendStr(String... args) {
        if (args.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : args) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 获取字符串中某个字符串的个数
     *
     * @param str 源字符串
     * @param key 目标字符串
     * @return
     */
    public static int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();

            count++;
        }
        return count;
    }


    public static boolean checkChinese(String str) {
        //只允许汉字、数字、字母、加密符号
        if(null==str){
            return false;
        }
        String regEx = "[^*a-zA-Z0-9\\u4E00-\\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return str.equals(m.replaceAll("").trim());
    }


    public static int dp2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return ((int) (displayMetrics.density * dp));
    }
}