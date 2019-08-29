package com.md1k.services.common.utils;

import java.util.regex.Pattern;

/**
 * @author vvk
 * @since 2019/8/9
 */
public class ValidateUtil {

    private static final String COUNT_RECORD_PATTERN = "(?!0)\\d{1,8}";
    private static final String BANK_CARD_TYPE_PATTERN = "0|1|2|4|[\\s]{0}";
    private static final String BANK_TYPE_PATTERN = "[0-9]{7}";
    private static final String CUST_CERTIFICATION_TYPE = "[0-9A-Z]{1}|[\\s]{0}";
    private static final String AMOUNT_PATTERN = "(?!0)\\d{1,15}";
    private static final String BANK_ACCT_TYPE = "1|2|[\\s]{0}";
    private static final String CARD_VALID_DATE = "([0][1-9]|[1][0-2])[0-9]{2}|[\\s]{0}";
    private static final String CARD_CVV2 = "[0-9]{3}|[\\s]{0}";
    private static final String BANK_ACCOUNT_NO = "[0-9-]{8,32}";
    private static final String ONE_OR_TWO_REQUIRED = "[1,2]{1}";
    private static final String CURRENCY_REGEXP = "([CNY]|[USD]|[EUR]|[HKD]){3}";

    private ValidateUtil() {}

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void hasLength(String text, int length, String message) {
        if (!hasLength((CharSequence) text, length)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text) {
        hasLength(text, 1, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasLength(String text, int length) {
        hasLength(text, length, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) {
        if (!hasText((CharSequence) text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String text) {
        hasText(text, "[Assertion failed] - this String argument[" + text + "] must have text; it must not be null, empty, or blank");
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i] == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }

    }

    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void isInstanceOf(Class clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    public static void isInstanceOf(Class type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class superType, Class subType) {
        isAssignable(superType, subType, "");
    }

    public static void isAssignable(Class superType, Class subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength((CharSequence) str, 1)) {
            return false;
        } else {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasLength(CharSequence str, int length) {
        return str != null && str.length() >= length;
    }

    public static void hasMaxLength(String str, int maxLength, String message) {
        if (null == str || "".equals(str.trim()) || !hasMaxLength(str, maxLength)) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 不能为空,且长度不能超过" + maxLength);
        }
    }

    public static boolean hasMaxLength(CharSequence str, int maxLength) {
        return str != null && str.length() > 0 && str.length() <= maxLength;
    }

    public static boolean isMatch(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    public static void isMatchCountRecord(String str, String message) {
        Pattern pattern = Pattern.compile("(?!0)\\d{1,8}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 格式不正确或者范围正确,范围为:[1-99999999]");
        }
    }

    public static void isMatchBankCardType(String str, String message) {
        Pattern pattern = Pattern.compile("0|1|2|4|[\\s]{0}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能为0、1、2或者4");
        }
    }

    public static void isMatchBankType(String str, String message) {
        Pattern pattern = Pattern.compile("[0-9]{7}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能为7位数字");
        }
    }

    public static void isMatchCustCertificationType(String str, String message) {
        Pattern pattern = Pattern.compile("[0-9A-Z]{1}|[\\s]{0}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能是[0-9]或者[A-Z]");
        }
    }

    public static void isMatchAmount(String str, String message) {
        Pattern pattern = Pattern.compile("(?!0)\\d{1,15}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 格式不正确或者范围不正确,范围为:[1-999999999999999]分");
        }
    }

    public static void isMatchBankAcctType(String str, String message) {
        Pattern pattern = Pattern.compile("1|2|[\\s]{0}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 只能是1或者2");
        }
    }

    public static void isMatchCardValidDate(String str, String message) {
        Pattern pattern = Pattern.compile("([0][1-9]|[1][0-2])[0-9]{2}|[\\s]{0}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 信用卡有效期格式错误,格式为[月月年年]");
        }
    }

    public static void isMatchCardCvv2(String str, String message) {
        Pattern pattern = Pattern.compile("[0-9]{3}|[\\s]{0}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 信用卡CVV2码格式不正确");
        }
    }

    public static void isMatchBankAccountNo(String str, String message) {
        Pattern pattern = Pattern.compile("[0-9-]{8,32}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 银行卡号须为8-32位的数字或者”-“组成");
        }
    }

    public static void isMatchStatus(String str, String message) {
        Pattern pattern = Pattern.compile("[1,2]{1}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 交易状态只能为1或者2");
        }
    }

    public static void isMatchCurrency(String str, String message) {
        Pattern pattern = Pattern.compile("([CNY]|[USD]|[EUR]|[HKD]){3}");
        if (!pattern.matcher(str).matches()) {
            throw new IllegalArgumentException("{" + message + "}[Assertion failed] - 币种只支持[CNY,USD,EUR,HKD]");
        }
    }

}
