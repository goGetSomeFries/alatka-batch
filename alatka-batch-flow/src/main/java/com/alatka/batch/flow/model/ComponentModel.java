package com.alatka.batch.flow.model;

import jakarta.validation.constraints.NotNull;

public class ComponentModel {

    @NotNull
    private Type type;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
