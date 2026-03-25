package com.alatka.batch.flow.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SplitModel extends ComponentModel {

    private String taskExecutor;

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

    public void setFlows(Map<String, FlowModel> flows) {
        this.flows = flows.values().stream().collect(Collectors.toList());
    }

}
