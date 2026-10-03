package com.alatka.batch.monitor.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class StepExecutionListReq {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "jobExecutionId 不能为空")
    private Long jobExecutionId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "index 不能为空")
    private Integer index;

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
