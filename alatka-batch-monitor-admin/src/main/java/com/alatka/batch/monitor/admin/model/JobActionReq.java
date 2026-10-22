package com.alatka.batch.monitor.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Job Action Req")
public class JobActionReq {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "endpoint 不能为空")
    private String endpoint;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "actionCode 不能为空")
    private String actionCode;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "jobExecutionId 不能为空")
    private Long jobExecutionId;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }
}
