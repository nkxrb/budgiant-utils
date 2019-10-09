package org.budgiant.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 代码编辑常用方法合集（包含数据类型转换，判空、获取随机数...）
 *
 * @author nkxrb
 * @since 2019/10/01
 */
public class EditUtil {

    /**
     * 私有化构造，禁止实例化
     */
    private EditUtil() {
    }

    /**
     * 判断对象是否为空或NULL
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean isEmptyOrNull(Object obj) {
        boolean result = false;
        if (obj == null) {
            result = true;
        } else if (!(obj instanceof String) && !(obj instanceof StringBuffer)) {
            if (obj instanceof Map) {
                result = ((Map) obj).isEmpty();
            } else if (obj instanceof Collection) {
                result = ((Collection) obj).isEmpty();
            } else if (obj.getClass().isArray()) {
                result = Array.getLength(obj) == 0;
            }
        } else {
            result = obj.toString().length() == 0;
        }

        return result;
    }

    /**
     * 对象转String
     *
     * @param obj 对象
     * @return String
     */
    public static String obj2String(Object obj) {
        if (null == obj || "null".equals(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * String对象
     *
     * @param obj String对象
     * @return int
     */
    public static int string2IntOrZero(Object obj) {
        String s = obj2String(obj);
        if (StringUtils.isNumericSpace(s)) {
            return Integer.parseInt(s);
        } else {
            return 0;
        }
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     */
    public static String md5(String text, String key) {
        //加密后的字符串
        return DigestUtils.md5Hex(text + key);
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(String text) {
        //加密后的字符串
        return DigestUtils.md5Hex(text);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     */
    public static boolean verifyMd5(String text, String key, String md5) {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        return md5Text.equalsIgnoreCase(md5);
    }

    /**
     * 获取请求IP地址
     *
     * @param request 网络请求
     * @return String
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!EditUtil.isEmptyOrNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!EditUtil.isEmptyOrNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
