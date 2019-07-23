package com.example.kvmmanger.common.util;

import java.util.Random;

/**
 * RandomUtil class
 *
 * @author QWL
 * @date 2018-08-15 0015
 */
public class RandomUtil {
    public static long getRandomNum() {
        Random rand = new Random();
        return System.currentTimeMillis() % 1000000 * 100000 + rand.nextInt(100000);
    }

    public static String getRandomNumString() {
        return String.valueOf(getRandomNum());
    }
}
