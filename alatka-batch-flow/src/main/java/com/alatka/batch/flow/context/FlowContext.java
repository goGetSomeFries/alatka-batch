package com.alatka.batch.flow.context;

public final class FlowContext {

    private FlowContext() {
    }

    public static FlowContext getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final FlowContext INSTANCE = new FlowContext();
    }

}
