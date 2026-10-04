package com.alatka.batch.monitor.admin.model;

import com.alatka.batch.infra.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Step Execution 分页请求")
public class StepExecutionPageReq extends PageReqMessage {

    private String stepName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "jobExecutionId 不能为空")
    private Long jobExecutionId;

    @Schema(hidden = true)
    private LocalDateTime createTimeLeft;

    @Schema(hidden = true)
    private LocalDateTime createTimeRight;

    private String createTimeRange;

    @Schema(hidden = true)
    private LocalDateTime endTimeLeft;

    @Schema(hidden = true)
    private LocalDateTime endTimeRight;

    private String endTimeRange;

    private List<String> status;

    private String exitCode;

    private String exitMessage;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
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

    public LocalDateTime getEndTimeLeft() {
        return endTimeLeft;
    }

    public void setEndTimeLeft(LocalDateTime endTimeLeft) {
        this.endTimeLeft = endTimeLeft;
    }

    public LocalDateTime getEndTimeRight() {
        return endTimeRight;
    }

    public void setEndTimeRight(LocalDateTime endTimeRight) {
        this.endTimeRight = endTimeRight;
    }

    public String getEndTimeRange() {
        return endTimeRange;
    }

    public void setEndTimeRange(String endTimeRange) {
        this.endTimeRange = endTimeRange;
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
