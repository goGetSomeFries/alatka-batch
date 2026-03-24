package com.alatka.batch.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class TestJob implements ApplicationListener<ContextRefreshedEvent> {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private JobOperator jobOperator;

    @Bean("job_test")
    public Job testJob() {
        return jobBuilderFactory.get("job_test").start(testStep()).build();
    }

    @Bean("step_test")
    public Step testStep() {
        return stepBuilderFactory.get("step_test").tasklet((contribution, chunkContext) -> {
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
