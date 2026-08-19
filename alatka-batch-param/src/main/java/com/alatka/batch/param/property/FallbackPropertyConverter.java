package com.alatka.batch.param.property;

import org.springframework.stereotype.Component;

@Component
public class FallbackPropertyConverter implements PropertyConverter {

    @Override
    public boolean matched(String value) {
        return false;
    }

    @Override
    public String convert(String value) {
        return value;
    }
}
