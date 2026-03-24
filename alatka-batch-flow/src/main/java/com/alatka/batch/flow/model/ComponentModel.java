package com.alatka.batch.flow.model;

public abstract class ComponentModel {

    private Type type;

    public static enum Type {
        Step(StepModel.class),
        Flow(FlowModel.class),
        Decision(DecisionModel.class),
        Split(SplitModel.class);

        private Class<? extends ComponentModel> clazz;

        Type(Class<? extends ComponentModel> clazz) {
            this.clazz = clazz;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
