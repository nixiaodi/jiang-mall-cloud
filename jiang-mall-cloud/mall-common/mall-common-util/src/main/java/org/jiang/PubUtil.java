package org.jiang;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PubUtil {
    /**
     * STRING_NULL constant
     */
    private final static String STRING_NULL = "-";

    /**
     * 匹配手机号码，支持+86和86开头
     */
    private static final String REGX_MOBILE = "^((\\+86)|(86))?(13|15|17|18|19)\\d{9}$";

    /**
     * 匹配邮箱账号
     */
    private static final String REGX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 匹配手机号(先支持13,15,17,18,19开头的手机号码)
     */
    public static boolean isMobileNumber(String inputStr) {
        return !PubUtil.isNull(inputStr) && inputStr.matches(REGX_MOBILE);
    }

    /**
     * 判断一个或多个对象是否为空
     * 只要判断的一个对象为空则返回true,否则返回false
     */
    public static boolean isNull(Object... values) {
        if (!PubUtil.isNotNullAndNotEmpty(values)) {
            return true;
        }
        for (Object value : values) {
            boolean flag;
            if (value instanceof Object[]) {
                flag = !isNotNullAndNotEmpty((Object[]) value);
            } else if (value instanceof Collection<?>) {
                flag = isNotNullAndNotEmpty((Collection<?>) value);
            } else if (value instanceof String) {
                flag = isOEmptyOrNull(value);
            } else {
                flag = (null == value);
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * is object empty or null
     */
    private static boolean isOEmptyOrNull(Object o) {
        return o == null || isSEmptyOrNull(o.toString());
    }

    /**
     * is string empty or null
     */
    private static boolean isSEmptyOrNull(String s) {
        return trimAndNullAsEmpty(s).length() <= 0;
    }

    /**
     * trim string and if null as empty string
     */
    private static String trimAndNullAsEmpty(String s) {
        if (s != null && !s.trim().equals(STRING_NULL)) {
            return s.trim();
        } else {
            return "";
        }
    }

    /**
     * 判断对象数组是否为空且数量大于0
     */
    private static boolean isNotNullAndNotEmpty(Object[] value) {
        boolean b = false;
        if (value != null && value.length > 0) {
            b = true;
        }
        return b;
    }

    /**
     * 判断对象集合（List,Set）是否为空并且数量大于0
     */
    private static Boolean isNotNullAndNotEmpty(Collection<?> value) {
        boolean b = false;
        if (null != value && !value.isEmpty()) {
            b = true;
        }
        return b;
    }

    /**
     * is email
     */
    public static boolean isEmail(String str) {
        boolean b = true;
        if (isSEmptyOrNull(str) || !str.matches(REGX_EMAIL)) {
            b = false;
        }
        return b;
    }

    /**
     * return uuid
     */
    public synchronized static String uuid() {
        return UUID.randomUUID().toString();
    }
}
