package com.alatka.batch.example.bean;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowConfig {

    @Bean("flow_test1")
    public Flow testFlow1(@Qualifier("step_test1") Step step) {
        return new FlowBuilder<SimpleFlow>("flow_test1").start(step).build();
    }

    @Bean("flow_test2")
    public Flow testFlow2(@Qualifier("step_test2") Step step) {
        return new FlowBuilder<SimpleFlow>("flow_test2").start(step).build();
    }


}
