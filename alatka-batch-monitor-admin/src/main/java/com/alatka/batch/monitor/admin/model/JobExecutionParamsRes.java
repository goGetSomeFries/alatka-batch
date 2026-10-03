package com.alatka.batch.monitor.admin.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class JobExecutionParamsRes {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String parameterName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String parameterType;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String parameterValue;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String identifying;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getIdentifying() {
        return identifying;
    }

    public void setIdentifying(String identifying) {
        this.identifying = identifying;
    }
}
