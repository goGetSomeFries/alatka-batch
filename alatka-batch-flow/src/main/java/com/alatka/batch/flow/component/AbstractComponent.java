package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public abstract class AbstractComponent<M extends ComponentModel> implements IComponent, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public Object join(ComponentModel model, Object builder) {
        if (builder.getClass() == JobBuilder.class) {
            return doJoin((M) model, (JobBuilder) builder);
        }
        if (builder.getClass() == SimpleJobBuilder.class) {
            return doJoin((M) model, (SimpleJobBuilder) builder);
        }
        if (builder.getClass() == JobFlowBuilder.class) {
            return doJoin((M) model, (JobFlowBuilder) builder);
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

    /**
     * get last step from SimpleJobBuilder
     *
     * @param simpleJobBuilder
     * @return
     */
    protected final Step getLastStep(SimpleJobBuilder simpleJobBuilder) {
        DirectFieldAccessor accessor = new DirectFieldAccessor(simpleJobBuilder);
        List<Step> list = (List<Step>) accessor.getPropertyValue("steps");
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("Steps list is null or empty");
        }
        return list.get(list.size() - 1);
    }

    protected abstract Class<M> modelClass();

    protected abstract Object doJoin(M model, JobBuilder jobBuilder);

    protected abstract Object doJoin(M model, SimpleJobBuilder simpleJobBuilder);

    protected abstract Object doJoin(M model, JobFlowBuilder jobFlowBuilder);
}
