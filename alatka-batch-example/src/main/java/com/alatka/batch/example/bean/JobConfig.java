package com.alatka.batch.example.bean;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    private JobRepository jobRepository;

    @Bean("job_test98")
    public Job jobTest98(@Qualifier("step_test11") Step step) {
        return new JobBuilder("job_test98", jobRepository).start(step).build();
    }

    @Bean("step_test11")
    public Step testStep11(@Qualifier("sub_job_test1") Job job) {
        return new StepBuilder("step_test11", jobRepository).job(job).build();
    }

    @Bean("sub_job_test1")
    public Job testSubJob1(@Qualifier("step_test1") Step step1, @Qualifier("step_test2") Step step2) {
        return new JobBuilder("sub_job_test1", jobRepository).start(step1).next(step2).build();
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
}
