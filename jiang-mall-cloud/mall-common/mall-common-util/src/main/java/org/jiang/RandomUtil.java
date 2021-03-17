package org.jiang;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomUtil {

    private static final int MAX_LENGTH = 50;

    /**
     * 生成一个随机验证码, 大小写字母+数字
     */
    public static String createComplexCode(int length) {
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH;
        }
        Random r = new Random();
        StringBuilder code = new StringBuilder();

        while (true) {
            if (code.length() == length) {
                break;
            }
            int temp = r.nextInt(127);
            if (temp < 32 || temp == 92 || temp == 47 || temp == 34) {
                continue;
            }
            char x = ((char) temp);
            if (code.toString().indexOf(x) > 0) {
                continue;
            }
            code.append(x);
        }

        return code.toString();
    }

    /**
     * 生成一个数字验证码
     */
    public static String createNumberCode(int length) {
        return randomString("0123456789",length);
    }

    /**
     * 根据给定的字符串生成随机的验证码
     */
    private static String randomString(String baseString,int length) {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();

        for (int i = 0; i < length; i++) {
            int number = r.nextInt(baseLength);
            code.append(baseString.charAt(number));
        }
        return code.toString();
    }
}
