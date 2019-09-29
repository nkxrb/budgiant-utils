package org.budgiant.common.utils;

import org.apache.commons.lang3.StringUtils;

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

}
