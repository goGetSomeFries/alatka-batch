package com.alatka.batch.flow.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SplitModel extends ComponentModel {

    @NotBlank
    private String taskExecutor;

    @NotEmpty
    private List<FlowModel> flows;

    public String getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(String taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public List<FlowModel> getFlows() {
        return flows;
    }

    public void setOriginFlows(List<FlowModel> flows) {
        this.flows = flows;
    }

    /**
     * Yaml工具解析为Map类型，需手动转换为List
     *
     * @param flows Map<String, FlowModel>类型
     */
    public void setFlows(Map<String, FlowModel> flows) {
        this.flows = flows.values().stream().collect(Collectors.toList());
    }

}
