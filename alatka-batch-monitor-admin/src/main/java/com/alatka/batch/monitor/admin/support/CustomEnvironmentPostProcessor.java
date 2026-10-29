package com.alatka.batch.monitor.admin.support;

import com.alatka.batch.monitor.AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> source = new HashMap<>();
        source.put(AutoConfiguration.SUB_JOB_PLUGIN_ENABLED, false);
        source.put(AutoConfiguration.OPERATOR_CONTROLLER_ENABLED, false);

        environment.getPropertySources().addLast(new MapPropertySource("alatka.batch.monitor", source));
    }
}
