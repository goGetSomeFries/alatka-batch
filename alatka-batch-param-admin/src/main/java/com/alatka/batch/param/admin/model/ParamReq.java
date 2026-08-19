package com.alatka.batch.param.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "参数请求")
public class ParamReq {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "name 不能为空")
    private String name;

    @Schema(description = "参数键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "key 不能为空")
    private String key;

    @Schema(description = "参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "value 不能为空")
    private String value;

    @Schema(description = "参数描述")
    private String desc;

    @Schema(description = "参数类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "value 不能为空")
    private String type;

    @Schema(description = "是否可用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "enabled 不能为空")
    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
