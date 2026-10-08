package com.alatka.batch.monitor;

import com.alatka.batch.monitor.jobstep.JobStepBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    public static final String SUB_JOB_PLUGIN_ENABLED = "alatka.batch.monitor.subJobPlugin.enabled";

    @Bean
    @ConditionalOnBooleanProperty(value = SUB_JOB_PLUGIN_ENABLED, matchIfMissing = true)
    public JobStepBeanPostProcessor jobStepBeanPostProcessor() {
        return new JobStepBeanPostProcessor();
    }
}
