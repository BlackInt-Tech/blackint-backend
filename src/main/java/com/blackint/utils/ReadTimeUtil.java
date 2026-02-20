package com.blackint.utils;

public class ReadTimeUtil {

    public static int calculate(String content) {
        int words = content.split("\\s+").length;
        return Math.max(1, words / 200);
    }
}