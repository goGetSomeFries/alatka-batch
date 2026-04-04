package com.alatka.batch.example.bean;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeciderConfig {

    @Bean("defaultDecider1")
    public JobExecutionDecider decider1() {
        return (jobExecution, stepExecution) -> stepExecution.getExitStatus() == ExitStatus.COMPLETED ?
                FlowExecutionStatus.COMPLETED : FlowExecutionStatus.FAILED;
    }

    @Bean("defaultDecider2")
    public JobExecutionDecider decider2() {
        return (jobExecution, stepExecution) -> stepExecution.getExitStatus() == ExitStatus.COMPLETED ?
                FlowExecutionStatus.COMPLETED : FlowExecutionStatus.FAILED;
    }
}
