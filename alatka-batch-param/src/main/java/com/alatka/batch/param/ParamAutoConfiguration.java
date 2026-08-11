package com.alatka.batch.param;

import com.alatka.batch.param.builder.BatchParamBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamAutoConfiguration {

    @Bean
    public BatchParamBuilder batchParamBuilder() {
        return new BatchParamBuilder();
    }

}
