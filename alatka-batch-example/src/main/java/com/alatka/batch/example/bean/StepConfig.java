package com.alatka.batch.example.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepConfig {

    private Logger logger = LoggerFactory.getLogger(StepConfig.class);

    private StepBuilderFactory stepBuilderFactory;

    @Bean("step_test1")
    public Step testStep1() {
        return stepBuilderFactory.get("step_test1").tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test1...");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test2")
    public Step testStep2() {
        return stepBuilderFactory.get("step_test2").tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test2...");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test3")
    public Step testStep3() {
        return stepBuilderFactory.get("step_test3").tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test3...");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test4")
    public Step testStep4() {
        return stepBuilderFactory.get("step_test4").tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test4...");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test5")
    public Step testStep5() {
        return stepBuilderFactory.get("step_test5").tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test5...");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
