package com.alatka.batch.param.admin.model;

import com.alatka.batch.infra.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "参数分页请求")
public class ParamPageReq extends PageReqMessage {

    @Schema(description = "参数名称")
    private String name;

    @Schema(description = "参数键")
    private String key;

    @Schema(description = "参数描述")
    private String desc;

    @Schema(description = "参数类型")
    private String type;

    @Schema(description = "是否可用")
    private Boolean enabled;

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
