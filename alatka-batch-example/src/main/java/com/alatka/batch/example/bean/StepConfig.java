package com.alatka.batch.example.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepConfig {

    private Logger logger = LoggerFactory.getLogger(StepConfig.class);

    private JobRepository jobRepository;

    private PlatformTransactionManager transactionManager;

    @Bean("step_test1")
    public Step testStep1() {
        return new StepBuilder("step_test1", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test1...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test2")
    public Step testStep2() {
        return new StepBuilder("step_test2", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test2...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test3")
    public Step testStep3() {
        return new StepBuilder("step_test3", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test3...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test4")
    public Step testStep4() {
        return new StepBuilder("step_test4", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test4...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test5")
    public Step testStep5() {
        return new StepBuilder("step_test5", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test5...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test6")
    public Step testStep6() {
        return new StepBuilder("step_test6", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test6...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test7")
    public Step testStep7() {
        return new StepBuilder("step_test7", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test7...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test8")
    public Step testStep8() {
        return new StepBuilder("step_test8", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test8...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test9")
    public Step testStep9() {
        return new StepBuilder("step_test9", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test9...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Bean("step_test10")
    public Step testStep10() {
        return new StepBuilder("step_test10", jobRepository).tasklet((contribution, chunkContext) -> {
            this.logger.info("执行step_test10...");
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
