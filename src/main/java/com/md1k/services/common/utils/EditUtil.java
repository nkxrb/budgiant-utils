package com.md1k.services.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * 数据类型编辑工具类
 *
 * @author vvk
 * @date 2018-08-16
 */
public class EditUtil {

    /**
     * 判断参数是否是数字
     *
     * @param value
     * @return
     */
    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从Object型转换成 String型，参数为Null的场合为空字符串
     *
     * @param value
     * @return
     */
    public static String objByNullToValue(Object value) {
        if (isEmptyOrNull(value))
            return "";
        String result = "";

        if ("null".equals(value)) {
            result = "";
        } else {
            result = value.toString() + "";
        }
        return result;
    }

    /**
     * 从 Integer型 转换成 int型，参数为Null和空文字列的场合为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static int intPByNullToZero(Integer value) {
        if (value == null)
            return 0;
        int result = 0;
        result = value.intValue();
        return result;
    }

    /**
     * 从 String型 转换成 int型，参数为Null和空文字列的场合为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static int intPByNullToZero(String value) {
        if (value == null) {
            return 0;
        }

        int result = 0;
        if ("".equals(value)) {
            result = 0;
        } else {
            result = Integer.parseInt(value);
        }
        return result;
    }

    /**
     * 从 Integer型 转换成 Integer型，参数为Null的场合为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Integer intByNullToZero(Integer value) {
        Integer retVal = value;
        if (retVal == null)
            retVal = Integer.valueOf(0);
        return retVal;
    }

    /**
     * 将String型转换成Integer型，参数为Null或者空字符串的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Integer intByNullToZero(String value) {
        return Integer.valueOf(strByNullToZero(value));
    }

    /**
     * 将String型转换成Integer型，参数为Null或者空字符串的场合返回值为Null
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Integer intByNullToNull(String value) {
        if ("".equals(strByNullToValue(value))) {
            return null;
        }

        return Integer.valueOf(value);
    }

    /**
     * 从 Long型 转换成 long型，参数为Null的场合为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static long longPByNullToZero(Long value) {
        if (value == null) {
            return 0L;
        }
        long result = 0L;
        result = value.longValue();
        return result;
    }

    /**
     * 将String型转换成long型，参数为Null或者空字符串的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static long longPByNullToZero(String value) {
        if (value == null) {
            return 0L;
        }
        long result = 0L;
        if ("".equals(value)) {
            result = 0L;
        } else {
            result = Long.parseLong(value);
        }
        return result;
    }

    /**
     * 将Long型转换成Long型，参数为Null的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Long longByNullToZero(Long value) {
        if (value == null) {
            return Long.valueOf(0L);
        }
        return value;
    }

    /**
     * 将String型转换成Long型，参数为Null或者空字符串的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Long longByNullToZero(String value) {
        return Long.valueOf(strByNullToZero(value));
    }

    public static Long longByNullToNull(String value) {
        if ("".equals(strByNullToValue(value))) {
            return null;
        }
        return Long.valueOf(value);
    }

    /**
     * 将String型转换成double型，参数为Null或者空字符串的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static double doublePByNullToZero(String value) {
        double result = 0.0D;
        if ((value != null) && (!"".equals(value)) && (!".".equals(value))) {
            result = Double.parseDouble(value);
        }
        return result;
    }

    /**
     * 将String型转换成Double型，参数为Null或者空字符串的场合返回值为0
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Double doubleByNullToZero(String value) {
        return Double.valueOf(strByNullToZero(value));
    }

    /**
     * 将String型转换成Double型，参数为Null或者空字符串的场合返回值为Null
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Double doubleByNullToNull(String value) {
        if ("".equals(strByNullToValue(value))) {
            return null;
        }
        return Double.valueOf(value);
    }

    /**
     * 将String型转换成Double型，参数为Null或者空字符串的场合返回值为Null
     *
     * @param value 变换前的字符串<br>
     * @return 变换后的数值
     */
    public static Double doubleByNullToZero(Double value) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (value == null) {
            return 0.0;
        }
        return Double.parseDouble(df.format(value));
    }

    /**
     * bigdByNullToZero
     *
     * @param value
     * @return
     */
    public static BigDecimal bigdByNullToZero(String value) {
        BigDecimal result = BigDecimal.valueOf(0L);
        if ((value != null) && (value.length() != 0)) {
            result = new BigDecimal(value);
        }
        return result;
    }

    /**
     * bigdByNullToNull
     *
     * @param value
     * @return
     */
    public static BigDecimal bigdByNullToNull(String value) {
        if ("".equals(strByNullToValue(value))) {
            return null;
        }
        return new BigDecimal(value);
    }

    /**
     * 如果第一个字符串为空，则使用第二个参数为默认值
     *
     * @param value        参数值
     * @param defaultValue 默认值
     * @return 结果
     */
    public static String strByNullToDefault(String value, String defaultValue) {
        if (EditUtil.isEmptyOrNull(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * strByNullToValue
     *
     * @param value
     * @return
     */
    public static String strByNullToValue(Integer value) {
        if (value == null) {
            return "";
        }
        return strByNullToValue(value.toString());
    }

    /**
     * strByNullToValue
     *
     * @param value
     * @return
     */
    public static String strByNullToValue(Long value) {
        if (value == null) {
            return "";
        }
        return strByNullToValue(value.toString());
    }

    /**
     * strByNullToValue
     *
     * @param value
     * @return
     */
    public static String strByNullToValue(String value) {
        if (value == null)
            return "";
        String result = "";

        if ("null".equals(value)) {
            result = "";
        } else {
            result = value;
        }
        return result;
    }

    /**
     * strByNullToZero
     *
     * @param value
     * @return
     */
    public static String strByNullToZero(Integer value) {
        if (value == null) {
            return "0";
        }
        return value.toString();
    }

    /**
     * strByNullToZero
     *
     * @param value
     * @return
     */
    public static String strByNullToZero(Long value) {
        if (value == null) {
            return "0";
        }
        return value.toString();
    }

    /**
     * strByNullToZero
     *
     * @param value
     * @return
     */
    public static String strByNullToZero(String value) {
        String result = strByNullToValue(value);
        if (result.equals("")) {
            result = "0";
        }
        return result;
    }

    /**
     * strByNullToOne
     *
     * @param value
     * @return
     */
    public static String strByNullToOne(String value) {
        String result = strByNullToValue(value);
        if (result.equals("")) {
            result = "1";
        }
        return result;
    }

    /**
     * strByZeroToValue
     *
     * @param value
     * @return
     */
    public static String strByZeroToValue(int value) {
        return strByZeroToValue(String.valueOf(value));
    }

    /**
     * strByZeroToValue
     *
     * @param value
     * @return
     */
    public static String strByZeroToValue(Integer value) {
        String valueStr = null;
        if (value != null) {
            valueStr = String.valueOf(value);
        }
        return strByZeroToValue(valueStr);
    }

    /**
     * strByZeroToValue
     *
     * @param value
     * @return
     */
    public static String strByZeroToValue(long value) {
        return strByZeroToValue(String.valueOf(value));
    }

    /**
     * strByZeroToValue
     *
     * @param value
     * @return
     */
    public static String strByZeroToValue(Long value) {
        String valueStr = null;
        if (value != null) {
            valueStr = String.valueOf(value);
        }
        return strByZeroToValue(valueStr);
    }

    /**
     * strByZeroToValue
     *
     * @param value
     * @return
     */
    public static String strByZeroToValue(String value) {
        String result = strByNullToValue(value);
        if ("0".equals(result)) {
            result = "";
        }
        return result;
    }

    /**
     * strByNullToNull
     *
     * @param value
     * @return
     */
    public static String strByNullToNull(Integer value) {
        String valueStr = null;
        if (value != null) {
            valueStr = String.valueOf(value);
        }
        return valueStr;
    }

    /**
     * strByNullToNull
     *
     * @param value
     * @return
     */
    public static String strByNullToNull(Long value) {
        String valueStr = null;
        if (value != null) {
            valueStr = String.valueOf(value);
        }
        return valueStr;
    }

    /**
     * zeroFormat
     *
     * @param moji
     * @param cnt
     * @return
     */
    public static String zeroFormat(String moji, int cnt) {
        StringBuilder sb = new StringBuilder();
        if ((moji != null) && (moji.length() > 0))
            sb.append(moji);
        for (int i = sb.length(); i < cnt; i++) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    /**
     * spaceFormat
     *
     * @param moji
     * @param cnt
     * @return
     */
    public static String spaceFormat(String moji, int cnt) {
        StringBuilder sb = new StringBuilder();
        if ((moji != null) && (moji.length() > 0))
            sb.append(moji);
        for (int i = sb.length(); i < cnt; i++) {
            sb.insert(0, " ");
        }
        return sb.toString();
    }

    /**
     * spaceCut
     *
     * @param value
     * @return
     */
    public static String spaceCut(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("^[ |　]+", "").replaceAll("[ |　]+$", "");
    }

    /**
     * subString
     *
     * @param text
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static String subString(String text, int beginIndex, int endIndex) {
        if ("".equals(strByNullToValue(text))) {
            return "";
        }

        int index1 = beginIndex;
        int index2 = endIndex;

        if (index1 < 0) {
            index1 = 0;
        }
        if (index2 > text.length()) {
            index2 = text.length();
        }
        if (index1 > text.length() - 1) {
            return "";
        }
        return text.substring(index1, index2);
    }

    /**
     * convertDecimal
     *
     * @param value
     * @return
     */
    public static String convertDecimal(String value) {
        String ret = value;
        if (value != null) {
            BigDecimal decimalValue = new BigDecimal(value);
            ret = decimalValue.toPlainString();
        }
        return ret;
    }

    /**
     * commaFormat
     *
     * @param value
     * @return
     */
    public static String commaFormat(String value) {
        return commaFormat(value, -1, "");
    }

    /**
     * commaFormat
     *
     * @param value
     * @param precision
     * @return
     */
    public static String commaFormat(String value, int precision) {
        return commaFormat(value, precision, "");
    }

    /**
     * commaFormat
     *
     * @param value
     * @param precision
     * @param minusHyoki
     * @return
     */
    public static String commaFormat(String value, int precision, String minusHyoki) {
        if ((value == null) || (value.length() <= 0)) {
            return "";
        }

        String result = "";
        String point = "";
        String down = "";
        String srcString = value;
        String[] vals = srcString.split("\\.");

        if (vals.length == 2) {
            srcString = vals[0];
            point = ".";
            down = vals[1];
        }

        if (!srcString.matches("^-?[0-9]+$")) {
            return srcString;
        }

        if (precision != -1) {
            point = ".";
            down = StringUtils.rightPad(down, precision, "0");
        }

        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(3);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);

        if ("-0".equals(srcString)) {
            result = "-0" + point + down;
        } else {
            result = df.format(Long.parseLong(srcString)) + point + down;
        }

        if ((minusHyoki != null) && (!minusHyoki.equals(""))) {
            result = result.replace("-", minusHyoki);
        }
        return result;
    }

    /**
     * 对Null或者空进行判断，
     *
     * @param value 检查对象<br>
     * @return boolean 检查结果（True：Null或空、False：非Null或空）
     */
    public static final boolean isEmptyOrNull(Object value) {
        boolean result = false;

        // 对是否为 Null或空进行检查
        if (value == null) {
            // 为Null的场合（包含数字）
            result = true;
        } else if (value instanceof String || value instanceof StringBuffer) {
            // String的场合
            result = value.toString().length() == 0;
        } else if (value instanceof Map) {
            // Map的场合
            result = ((Map<?, ?>) value).isEmpty();
        } else if (value instanceof Collection) {
            // 集合的场合
            result = ((Collection<?>) value).isEmpty();
        } else if (value.getClass().isArray()) {
            // 数组的场合
            result = Array.getLength(value) == 0;
        }

        // 返回结果
        return result;
    }

    /**
     * 清除字符串中所有的空格
     *
     * @param value
     * @return
     */
    public static final String replaceAllBlank(String value) {
        if (isEmptyOrNull(value)) {
            return "";
        } else {
            return value.replaceAll(" ", "");
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 从Object型转换成 String型，参数为Null的场合为空字符串
     *
     * @param value
     * @return
     */
    public static String objByNullToZero(Object value) {
        if (isEmptyOrNull(value))
            return "0";
        String result = "";

        if ("null".equals(value)) {
            result = "0";
        } else {
            result = value.toString() + "";
        }
        return result;
    }

    /**
     * 查询点的经纬度范围
     *
     * @param longitude
     * @param latitude
     * @param dis
     * @return
     */
    public static List<Double> dislatlng(double longitude, double latitude, double dis) {
        List<Double> retList = new ArrayList<>();
        //先计算查询点的经纬度范围
        double r = 6370.856;//地球半径千米
        double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180));
        dlng = dlng * 180 / Math.PI;//角度转为弧度
        double dlat = dis / r;
        dlat = dlat * 180 / Math.PI;
        retList.add(latitude - dlat);
        retList.add(latitude + dlat);
        retList.add(longitude - dlng);
        retList.add(longitude + dlng);
        return retList;
    }

    /**
     * 获取制定长度的随机数
     *
     * @param length
     * @return
     */
    public static String getRandomCode(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
