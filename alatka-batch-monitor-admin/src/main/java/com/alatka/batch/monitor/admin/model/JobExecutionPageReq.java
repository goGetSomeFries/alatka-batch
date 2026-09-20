package com.alatka.batch.monitor.admin.model;

import com.alatka.batch.infra.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Job Execution 分页请求")
public class JobExecutionPageReq extends PageReqMessage {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "jobName 不能为空")
    private String jobName;

    @Schema(hidden = true)
    private LocalDateTime createTimeLeft;

    @Schema(hidden = true)
    private LocalDateTime createTimeRight;

    private String createTimeRange;

    private List<String> status;

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

    public String getCreateTimeRange() {
        return createTimeRange;
    }

    public void setCreateTimeRange(String createTimeRange) {
        this.createTimeRange = createTimeRange;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
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
