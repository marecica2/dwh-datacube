package org.bmsource.dwh.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StringUtilsTest {

    @Test
    public void testNormalize() {
        Assertions.assertEquals("Some Text", StringUtils.normalize("$%some *(  text  "));
        Assertions.assertNull(StringUtils.normalize(null));
    }

    @Test
    public void testPrefixParsing() {
        List<String> prefixes = new ArrayList<String>() {{
            add("sum");
            add("avg");
            add("count");
        }};

        Assertions.assertArrayEquals(new String[]{"sum", "someField"}, StringUtils.splitByPrefix(prefixes,
            "sumSomeField"));
        Assertions.assertArrayEquals(new String[]{"avg", "avgField"}, StringUtils.splitByPrefix(prefixes,
            "avgAvgField"));
    }
}
