package com.alatka.batch.monitor.admin.model;

import com.alatka.batch.infra.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Step Execution 分页请求")
public class StepExecutionPageReq extends PageReqMessage {

    private String stepName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "jobExecutionId 不能为空")
    private Long jobExecutionId;

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
}
