package com.alatka.batch.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultJobExecutionListener2 implements JobExecutionListener {

    private Logger logger = LoggerFactory.getLogger(DefaultJobExecutionListener2.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        this.logger.info("Job execution started");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        this.logger.info("Job execution ended");
    }
}