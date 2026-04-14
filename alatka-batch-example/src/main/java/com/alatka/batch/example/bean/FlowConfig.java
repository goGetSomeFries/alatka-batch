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
    public Flow testFlow1(@Qualifier("step_test1") Step step1, @Qualifier("step_test2") Step step2) {
        return new FlowBuilder<SimpleFlow>("flow_test1").start(step1).next(step2).build();
    }

    @Bean("flow_test2")
    public Flow testFlow2(@Qualifier("step_test3") Step step3, @Qualifier("step_test4") Step step4) {
        return new FlowBuilder<SimpleFlow>("flow_test2").start(step3).next(step4).build();
    }

    @Bean("flow_test3")
    public Flow testFlow3(@Qualifier("step_test5") Step step5, @Qualifier("step_test6") Step step6) {
        return new FlowBuilder<SimpleFlow>("flow_test3").start(step5).next(step6).build();
    }

    @Bean("flow_test4")
    public Flow testFlow4(@Qualifier("step_test7") Step step7, @Qualifier("step_test8") Step step8) {
        return new FlowBuilder<SimpleFlow>("flow_test4").start(step7).next(step8).build();
    }

    @Bean("flow_test5")
    public Flow testFlow5(@Qualifier("step_test9") Step step9, @Qualifier("step_test10") Step step10) {
        return new FlowBuilder<SimpleFlow>("flow_test5").start(step9).next(step10).build();
    }
}
