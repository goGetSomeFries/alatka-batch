package com.alatka.batch.example;

import com.alatka.batch.flow.FlowAutoConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;

@Configuration
public class TestJob {

    private JobBuilderFactory jobBuilderFactory;


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
    public Job job2(@Qualifier(FlowAutoConfiguration.STEP_PASSTHROUGH) Step step1, Flow flow_test1, Flow flow_test2) {
        Job job = jobBuilderFactory.get("job2").start(step1).split(new SyncTaskExecutor()).add(flow_test1, flow_test2).end().build();
        return job;
    }

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }


}
