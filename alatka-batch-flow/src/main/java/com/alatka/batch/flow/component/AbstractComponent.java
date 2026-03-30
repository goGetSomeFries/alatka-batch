package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;

public abstract class AbstractComponent<M extends ComponentModel> implements IComponent {

    @Override
    public Object test(ComponentModel model, Object builder) {
        if (builder.getClass() == JobBuilder.class) {
            return doTest((M) model, (JobBuilder) builder);
        }
        if (builder.getClass() == SimpleJobBuilder.class) {
            return doTest((M) model, (SimpleJobBuilder) builder);
        }
        if (builder.getClass() == JobFlowBuilder.class) {
            return doTest((M) model, (JobFlowBuilder) builder);
        }
        throw new IllegalArgumentException("Unsupported builder type: " + builder.getClass());
    }

    protected abstract Object doTest(M model, JobBuilder jobBuilder);

    protected abstract Object doTest(M model, SimpleJobBuilder simpleJobBuilder);

    protected abstract Object doTest(M model, JobFlowBuilder jobFlowBuilder);
}
