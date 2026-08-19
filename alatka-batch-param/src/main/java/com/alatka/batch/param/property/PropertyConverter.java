package com.alatka.batch.param.property;

public interface PropertyConverter {

    String PREFIX = "${";

    String SUFFIX = "}";

    boolean matched(String value);

    String convert(String value);

}
