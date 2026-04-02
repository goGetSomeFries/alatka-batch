package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractComponent<M extends ComponentModel> implements IComponent, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public Wrapper join(ComponentModel model, Object builder, Object lastOne) {
        if (builder.getClass() == JobBuilder.class) {
            return doJoin((M) model, (JobBuilder) builder, lastOne);
        }
        if (builder.getClass() == SimpleJobBuilder.class) {
            return doJoin((M) model, (SimpleJobBuilder) builder, lastOne);
        }
        if (builder.getClass() == JobFlowBuilder.class) {
            return doJoin((M) model, (JobFlowBuilder) builder, lastOne);
        }
        throw new IllegalArgumentException("Unsupported builder type: " + builder.getClass());
    }

    @Override
    public Job build(Object builder) {
        if (builder.getClass() == SimpleJobBuilder.class) {
            SimpleJobBuilder simpleJobBuilder = (SimpleJobBuilder) builder;
            return simpleJobBuilder.build();
        }
        if (builder.getClass() == JobFlowBuilder.class) {
            JobFlowBuilder jobFlowBuilder = (JobFlowBuilder) builder;
            return jobFlowBuilder.end().build();
        }
        throw new IllegalArgumentException("Unsupported builder type: " + builder.getClass());
    }

    @Override
    public boolean matched(ComponentModel model) {
        return modelClass() == model.getClass();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected abstract Class<M> modelClass();

    protected abstract Wrapper doJoin(M model, JobBuilder jobBuilder, Object lastOne);

    protected abstract Wrapper doJoin(M model, SimpleJobBuilder simpleJobBuilder, Object lastOne);

    protected abstract Wrapper doJoin(M model, JobFlowBuilder jobFlowBuilder, Object lastOne);
}
