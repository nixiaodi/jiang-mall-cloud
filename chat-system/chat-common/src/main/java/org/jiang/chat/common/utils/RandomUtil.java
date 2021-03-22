package org.jiang.chat.common.utils;

public class RandomUtil {

    public static int getRandom() {

        double random = Math.random();
        return (int) (random * 100);
    }
}
