package com.alatka.batch.flow.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "流程组请求")
public class FlowGroupReq {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "关键字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "key 不能为空")
    private String key;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "name 不能为空")
    private String name;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "enabled 不能为空")
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
