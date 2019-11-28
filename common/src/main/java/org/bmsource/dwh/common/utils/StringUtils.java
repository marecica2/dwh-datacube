package org.bmsource.dwh.common.utils;

public class StringUtils {
    public static String normalize(String str) {
        String ascii = str.replaceAll("[^\\x00-\\x7F]", "");
        ascii = ascii.trim().replaceAll(" +", " ");
        return ascii;
    }
}
