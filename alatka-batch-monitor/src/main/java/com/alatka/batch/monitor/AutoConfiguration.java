package com.alatka.batch.monitor;

import com.alatka.batch.monitor.jobstep.JobStepBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    public static final String JOB_STEP_BEAN_POST_PROCESSOR_ENABLED = "alatka.batch.monitor.jobStepBeanPostProcessor.enabled";

    @Bean
    @ConditionalOnProperty(value = JOB_STEP_BEAN_POST_PROCESSOR_ENABLED, matchIfMissing = true)
    public JobStepBeanPostProcessor jobStepBeanPostProcessor() {
        return new JobStepBeanPostProcessor();
    }
}
