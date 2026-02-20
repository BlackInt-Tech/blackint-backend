package com.blackint.utils;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(String prefix) {

        StringBuilder builder = new StringBuilder(prefix);
        builder.append("-");

        for (int i = 0; i < 20; i++) {
            builder.append(ALPHA_NUM.charAt(RANDOM.nextInt(ALPHA_NUM.length())));
        }
        return builder.toString();
    }
}
