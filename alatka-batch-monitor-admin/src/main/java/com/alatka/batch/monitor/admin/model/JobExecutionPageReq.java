package com.alatka.batch.monitor.admin.model;

import com.alatka.batch.infra.model.PageReqMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "Job Execution 分页请求")
public class JobExecutionPageReq extends PageReqMessage {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "jobName 不能为空")
    private String jobName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeLeft;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeRight;

    private String status;

    private String exitCode;

    private String exitMessage;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public LocalDateTime getCreateTimeLeft() {
        return createTimeLeft;
    }

    public void setCreateTimeLeft(LocalDateTime createTimeLeft) {
        this.createTimeLeft = createTimeLeft;
    }

    public LocalDateTime getCreateTimeRight() {
        return createTimeRight;
    }

    public void setCreateTimeRight(LocalDateTime createTimeRight) {
        this.createTimeRight = createTimeRight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExitCode() {
        return exitCode;
    }

    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }
}
