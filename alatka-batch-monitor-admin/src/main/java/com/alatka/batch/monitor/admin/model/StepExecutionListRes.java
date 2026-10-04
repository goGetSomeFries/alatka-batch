package com.alatka.batch.monitor.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class StepExecutionListRes {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private List<StepExecutionRes> list;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean hasNext;

    public List<StepExecutionRes> getList() {
        return list;
    }

    public void setList(List<StepExecutionRes> list) {
        this.list = list;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
