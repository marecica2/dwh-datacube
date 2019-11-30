package org.bmsource.dwh.common.utils;

import org.apache.commons.text.WordUtils;

public class StringUtils {
    public static String normalize(String str) {
        String ascii = str.replaceAll("[^\\d\\w\\s]", "");
        ascii = ascii.trim().replaceAll(" +", " ");
        ascii = ascii.toLowerCase();
        return WordUtils.capitalize(ascii);
    }
}
