package com.alatka.batch.param.property;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class JobDatePropertyConverter extends VariablePropertyConverter {

    @Override
    protected String identity() {
        return "jobDate";
    }

    @Override
    public String convert(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.now().format(formatter);
    }
}
