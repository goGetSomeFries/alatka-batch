package com.alatka.batch.flow.model;

import java.util.List;

public class DecisionModel extends BeanComponentModel {

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

    public void setFlows(List<FlowModel> flows) {
        this.flows = flows;
    }
}
