package org.bmsource.dwh.common.utils;

import org.apache.commons.text.WordUtils;

import java.util.List;

public class StringUtils {
    public static String normalize(String str) {
        if(str == null)
            return null;
        String ascii = str.replaceAll("[^-\\.\\d\\w\\s]", "");
        ascii = ascii.trim().replaceAll(" +", " ");
        ascii = ascii.toLowerCase();
        return WordUtils.capitalize(ascii);
    }


    public static String[] splitByPrefix(List<String> prefixes, String value) {
        for (String prefix : prefixes) {
            if (value.startsWith(prefix)) {
                String[] splitBy = new String[2];
                splitBy[0] = prefix;
                splitBy[1] = org.apache.commons.lang3.StringUtils
                    .uncapitalize(value.substring(value.indexOf(prefix) + prefix.length()));
                return splitBy;
            }
        }
        return null;
    }
}
