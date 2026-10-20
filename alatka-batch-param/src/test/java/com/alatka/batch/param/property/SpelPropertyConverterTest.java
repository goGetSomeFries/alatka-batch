package com.alatka.batch.param.property;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SpelPropertyConverterTest {

    private SpelPropertyConverter converter = new SpelPropertyConverter();

    @Test
    @DisplayName("字符串")
    public void testConvert01() {
        String value = "${spel:'abc'}";
        Assertions.assertEquals("abc", converter.convert(value));
    }

    @Test
    @DisplayName("数字")
    public void testConvert02() {
        String value = "${spel:3}";
        Assertions.assertEquals("3", converter.convert(value));
    }

    @Test
    @DisplayName("表达式")
    public void testConvert03() {
        String value = "${spel:3 * 6}";
        Assertions.assertEquals("18", converter.convert(value));
    }

    @Test
    @DisplayName("表达式")
    public void testConvert04() {
        String value = "${spel:T(java.time.LocalDate).now().toString()}";
        Assertions.assertEquals(LocalDate.now().toString(), converter.convert(value));
    }
}
