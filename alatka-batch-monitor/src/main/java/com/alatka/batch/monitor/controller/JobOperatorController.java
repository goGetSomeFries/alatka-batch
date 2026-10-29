package com.alatka.batch.monitor.controller;

import com.alatka.batch.infra.model.ResMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Spring Batch Job Operator")
@RestController
@RequestMapping("/batch/monitor/job")
public class JobOperatorController {

    private JobOperator jobOperator;

    @Operation(summary = "重启 job")
    @PostMapping("/restart/{jobExecutionId}")
    public ResMessage<Void> restart(@PathVariable Long jobExecutionId) {
        return ResMessage.success(() -> {
            try {
                jobOperator.restart(jobExecutionId);
            } catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException |
                     JobRestartException | JobParametersInvalidException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    @Operation(summary = "停止 job")
    @PostMapping("/stop/{jobExecutionId}")
    public ResMessage<Void> stop(@PathVariable Long jobExecutionId) {
        return ResMessage.success(() -> {
            try {
                jobOperator.stop(jobExecutionId);
            } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    @Operation(summary = "放弃 job")
    @PostMapping("/abandon/{jobExecutionId}")
    public ResMessage<Void> abandon(@PathVariable Long jobExecutionId) {
        return ResMessage.success(() -> {
            try {
                jobOperator.abandon(jobExecutionId);
            } catch (JobExecutionAlreadyRunningException | NoSuchJobExecutionException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }
}
