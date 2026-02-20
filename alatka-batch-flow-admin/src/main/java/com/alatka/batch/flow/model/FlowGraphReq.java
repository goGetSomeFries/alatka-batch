package com.alatka.batch.flow.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "流程请求")
public class FlowGraphReq {

    @Schema(description = "流程主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "flowId 不能为空")
    private Long flowId;

    @Schema(description = "流程图数据", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "data 不能为空")
    private String data;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
