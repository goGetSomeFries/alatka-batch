package com.alatka.batch.flow.model.group;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "流程组响应")
public class FlowGroupRes {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String key;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
