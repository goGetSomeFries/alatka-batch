package com.alatka.batch.monitor.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "BATCH_STEP_EXECUTION")
public class StepExecution {

    @Id
    private Long stepExecutionId;
    private Long version;
    private String stepName;
    private Long jobExecutionId;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer commitCount;
    private Integer readCount;
    private Integer filterCount;
    private Integer writeCount;
    private Integer readSkipCount;
    private Integer writeSkipCount;
    private Integer processSkipCount;
    private Integer rollbackCount;
    private String exitCode;
    private String exitMessage;

    public Long getStepExecutionId() {
        return stepExecutionId;
    }

    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(Integer commitCount) {
        this.commitCount = commitCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Integer filterCount) {
        this.filterCount = filterCount;
    }

    public Integer getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(Integer writeCount) {
        this.writeCount = writeCount;
    }

    public Integer getReadSkipCount() {
        return readSkipCount;
    }

    public void setReadSkipCount(Integer readSkipCount) {
        this.readSkipCount = readSkipCount;
    }

    public Integer getWriteSkipCount() {
        return writeSkipCount;
    }

    public void setWriteSkipCount(Integer writeSkipCount) {
        this.writeSkipCount = writeSkipCount;
    }

    public Integer getProcessSkipCount() {
        return processSkipCount;
    }

    public void setProcessSkipCount(Integer processSkipCount) {
        this.processSkipCount = processSkipCount;
    }

    public Integer getRollbackCount() {
        return rollbackCount;
    }

    public void setRollbackCount(Integer rollbackCount) {
        this.rollbackCount = rollbackCount;
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
