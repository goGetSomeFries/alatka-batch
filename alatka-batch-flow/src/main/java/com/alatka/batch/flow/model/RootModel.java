package com.alatka.batch.flow.model;

import java.util.List;
import java.util.Map;

public class RootModel {

    private boolean enabled = true;

    private String name;

    private String desc;

    private List<Map<Type, Map<String, Object>>> steps;

    public static enum Type {
        STEP(StepModel.class),
        FLOW(FlowModel.class),
        DECISION(DecisionModel.class),
        SPLIT(SplitModel.class);

        private Class<? extends ComponentModel> clazz;

        Type(Class<? extends ComponentModel> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends ComponentModel> getClazz() {
            return clazz;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Map<Type, Map<String, Object>>> getSteps() {
        return steps;
    }

    public void setSteps(List<Map<Type, Map<String, Object>>> steps) {
        this.steps = steps;
    }
}
