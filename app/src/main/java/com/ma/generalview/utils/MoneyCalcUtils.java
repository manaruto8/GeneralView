package com.ma.generalview.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Aeolus on 2018/10/23.
 */

public class MoneyCalcUtils {
    public static final int TYPE_ADD = 0x000; // 加法
    public static final int TYPE_MULTIPLY = 0x001; // 乘法
    public static final int TYPE_DIVIDE = 0x002; // 除法
    public static final int TYPE_SUBTRACT = 0x003; // 减法
    public static final int SCALE = 2;
    /**
     *  加法
     * @param a
     * @param b
     * @return
     */
    public static Double add(Double a, Double b) {
        return calc(a, b, SCALE, TYPE_ADD, null);
    }

    public static Double add(Double a, int b) {
        BigDecimal c = new BigDecimal(b);
        return calc(a, c.doubleValue(), SCALE, TYPE_ADD, null);
    }
    /**
     * 减法
     * @param a
     * @param b
     * @return
     */

    public static Double sub(Double a, Double b) {
        return calc(a, b, SCALE, TYPE_SUBTRACT, null);
    }

    public static Double sub(Double a, int b) {
        BigDecimal c = new BigDecimal(b);
        return calc(a, c.doubleValue(), SCALE, TYPE_SUBTRACT, null);
    }
    /**
     * 乘法
     * @param a
     * @param b
     * @return
     */

    public static Double multiply(Double a, Double b) {
        return calc(a, b, SCALE, TYPE_MULTIPLY, null);
    }

    public static Double multiply(Double a, int b) {
        BigDecimal c = new BigDecimal(b);
        return calc(a, c.doubleValue(), SCALE, TYPE_MULTIPLY, null);
    }

    /**
     * 除法
     * @param a
     * @param b
     * @return
     */

    public static Double divide(Double a, Double b) {
        return calc(a, b, SCALE, TYPE_DIVIDE, null);
    }

    /**
     * 乘法
     * @param a
     * @param b
     * @param scale 小数点后保留的位数
     * @param mode 保留的模式
     * @return
     */
    public static Double multiply(Double a, Double b, int scale, RoundingMode mode) {

        return calc(a, b, scale, TYPE_MULTIPLY, mode);
    }
    /**
     * 除法
     * @param a
     * @param b
     * @param scale 小数点后保留的位数
     * @param mode 保留的模式
     * @return
     */
    public static Double divide(Double a, Double b, int scale, RoundingMode mode) {

        return calc(a, b, scale, TYPE_DIVIDE, mode);
    }
    /**
     *  计算
     * @param a
     * @param b
     * @param scale
     * @param type
     * @param mode
     * @return
     */
    private static Double calc(Double a, Double b, int scale, int type, RoundingMode mode) {
        BigDecimal result = null;

        BigDecimal bgA = new BigDecimal(String.valueOf(a));
        BigDecimal bgB = new BigDecimal(String.valueOf(b));
        switch (type) {
            case TYPE_ADD:
                result = bgA.add(bgB);
                break;
            case TYPE_MULTIPLY:
                result = bgA.multiply(bgB);
                break;
            case TYPE_DIVIDE:
                try {
                    result = bgA.divide(bgB);
                } catch (ArithmeticException e) {// 防止无限循环而报错  采用四舍五入保留3位有效数字
                    result = bgA.divide(bgB,3, RoundingMode.HALF_DOWN);
                }

                break;
            case TYPE_SUBTRACT:
                result = bgA.subtract(bgB);
                break;

        }
        if (mode==null) {

            result = result.setScale(scale, RoundingMode.HALF_UP);
        }else{
            result = result.setScale(scale,mode);

        }
        return Double.parseDouble(result.toString());
    }

    public static Double abs(Double a) {
        return new BigDecimal(a).abs().doubleValue();
    }

}
