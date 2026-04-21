package com.alatka.batch.flow.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "流程部署请求")
public class FlowDeployReq {

    @Schema(description = "uri集合", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "uris 不能为空")
    private List<String> uris;

    @Schema(description = "路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "path 不能为空")
    private String path;

    @Schema(description = "流程", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "flowIdList 不能为空")
    private List<Long> flowIdList;

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Long> getFlowIdList() {
        return flowIdList;
    }

    public void setFlowIdList(List<Long> flowIdList) {
        this.flowIdList = flowIdList;
    }
}
