package com.alatka.batch.flow.model.group;

import com.alatka.batch.flow.model.PageReqMessage;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "流程组请求")
public class FlowGroupPageReq extends PageReqMessage {

    @Schema(description = "关键字")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否可用")
    private Boolean enabled;

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
