package com.alatka.batch.monitor.admin.support;

import com.alatka.batch.monitor.AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;

public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.getPropertySources().addLast(
                new MapPropertySource("alatkaDefault", Collections.singletonMap(AutoConfiguration.JOB_STEP_BEAN_POST_PROCESSOR_ENABLED, false))
        );
    }
}
