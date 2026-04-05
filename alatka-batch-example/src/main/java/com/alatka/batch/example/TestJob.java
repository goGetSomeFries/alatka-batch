package com.alatka.batch.example;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.SyncTaskExecutor;

@Configuration
public class TestJob implements ApplicationListener<ContextRefreshedEvent> {

    private JobBuilderFactory jobBuilderFactory;

    private JobOperator jobOperator;


    @Bean("job_test")
    public Job testJob(Flow flow_test1, Flow flow_test2, Step step_test1, Step step_test2, JobExecutionDecider defaultDecider1) {
        Job job = jobBuilderFactory.get("job_test")
                .start(step_test1)
                .on("COMPLETED").to(flow_test1)
                .from(step_test1)
                .on("FAILED").fail()
                .from(flow_test1)
                .next(step_test2)
                .end()
                .build();
        return job;
    }

    @Bean("job2")
    public Job job2(Step step_test1, Step step_test2, Flow flow_test1, Flow flow_test2) {
        Flow splitFlow = new FlowBuilder<Flow>("default1").split(new SyncTaskExecutor()).add(flow_test1, flow_test2).end();
        Job job = jobBuilderFactory.get("job2").start(step_test1).on("COMPLETED").to(splitFlow).from(step_test1).on("*").fail()
                .from(splitFlow).next(step_test2).end().build();
        return job;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
/*
        try {
            jobOperator.start("job2", "x=4");
        } catch (NoSuchJobException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyExistsException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
*/
    }
}
