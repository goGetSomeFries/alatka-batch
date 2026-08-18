package com.alatka.batch.param.property;

import org.springframework.stereotype.Component;

@Component
public class RawPropertyConverter implements PropertyConverter {

    @Override
    public boolean matched(String value) {
        return value == null || !value.startsWith(PropertyConverter.PREFIX);
    }

    @Override
    public String convert(String value) {
        return value;
    }
}
