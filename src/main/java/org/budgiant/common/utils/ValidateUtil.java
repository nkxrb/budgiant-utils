package org.budgiant.common.utils;

import java.util.regex.Pattern;

/**
 * 校验工具类(支持常用数据类型以及自定义正则校验)
 *
 * @author nkxrb
 * @since 2019/8/9
 */
public class ValidateUtil {

    /**
     * 校验邮箱格式
     */
    private static final String EMAIL_PATTERN = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 校验手机号
     */
    private static final String MOBILE_PATTERN = "0?(13|14|15|17|18|19)[0-9]{9}";
    /**
     * 校验电话（包含座机）
     */
    private static final String PHONE_PATTERN = "[0-9-()（）]{7,18}";


    /**
     * 私有化构造，禁止实例化
     */
    private ValidateUtil() {
    }

    /**
     * 校验正则匹配
     *
     * @param str   校验字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean isMatch(String str, String regex) {
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 校验邮箱
     *
     * @param str 校验字符串
     * @return boolean
     */
    public static boolean isEmail(String str) {
        return isMatch(str, EMAIL_PATTERN);
    }

    /**
     * 校验手机号（中国大陆）
     *
     * @param str 校验字符串
     * @return boolean
     */
    public static boolean isMobile(String str) {
        return isMatch(str, MOBILE_PATTERN);
    }

    /**
     * 校验电话（中国大陆）
     *
     * @param str 校验字符串
     * @return boolean
     */
    public static boolean isPhone(String str) {
        return isMatch(str, PHONE_PATTERN);
    }

}
