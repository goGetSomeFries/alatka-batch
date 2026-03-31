package com.alatka.batch.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TestJob implements ApplicationListener<ContextRefreshedEvent> {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private JobOperator jobOperator;

    @Bean("job_test")
    public Job testJob() {
        return jobBuilderFactory.get("job_test").start(testFlow1()).end().build();
    }

    @Bean("flow_test1")
    public Flow testFlow1() {
        return new FlowBuilder<SimpleFlow>("flow_test1").start(testStep2()).build();
    }

    @Bean("flow_test2")
    public Flow testFlow2() {
        return new FlowBuilder<SimpleFlow>("flow_test2").start(testStep3()).build();
    }

    @Bean("step_test1")
    public Step testStep1() {
        return stepBuilderFactory.get("step_test1").tasklet((contribution, chunkContext) -> {
            System.out.println(contribution);
            System.out.println(chunkContext);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test2")
    public Step testStep2() {
        return stepBuilderFactory.get("step_test2").tasklet((contribution, chunkContext) -> {
            System.out.println(contribution);
            System.out.println(chunkContext);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean("step_test3")
    public Step testStep3() {
        return stepBuilderFactory.get("step_test3").tasklet((contribution, chunkContext) -> {
            System.out.println(contribution);
            System.out.println(chunkContext);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @Bean
    public TaskExecutor defaultTaskExecutor() {
        return new SyncTaskExecutor();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        try {
//            this.jobOperator.start("job_test", "x=4");
//        } catch (NoSuchJobException e) {
//            throw new RuntimeException(e);
//        } catch (JobInstanceAlreadyExistsException e) {
//            throw new RuntimeException(e);
//        } catch (JobParametersInvalidException e) {
//            throw new RuntimeException(e);
//        }
    }
}
