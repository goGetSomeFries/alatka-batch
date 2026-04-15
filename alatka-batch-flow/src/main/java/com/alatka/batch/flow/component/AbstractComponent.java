package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public abstract class AbstractComponent<M extends ComponentModel> implements IComponent, ApplicationContextAware, BeanNameAware {

    protected ApplicationContext applicationContext;

    private String beanName;

    private AtomicInteger counter = new AtomicInteger(0);

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
        if (builder.getClass() == FlowBuilder.TransitionBuilder.class) {
            return doJoin((M) model, (FlowBuilder.TransitionBuilder<FlowJobBuilder>) builder);
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

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
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

    protected final Step createPassthroughStep() {
        String stepName = String.format("%s.%s.%s", this.beanName, "passthroughStep", counter.getAndIncrement());
        JobRepository jobRepository = applicationContext.getBean(JobRepository.class);
        PlatformTransactionManager transactionManager = applicationContext.getBean(PlatformTransactionManager.class);
        return new StepBuilder(stepName, jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager).build();
    }

    protected final Flow createFlow(String flowName, Function<FlowBuilder<Flow>, Flow> function) {
        String finalName = String.format("%s.%s.%s", this.beanName, flowName, counter.getAndIncrement());
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>(finalName);
        return function.apply(flowBuilder);
    }

    /**
     * {@link ComponentModel} class类型
     *
     * @return {@link ComponentModel} class类型
     */
    protected abstract Class<M> modelClass();

    protected abstract Object doJoin(M model, JobBuilder jobBuilder);

    protected abstract Object doJoin(M model, SimpleJobBuilder simpleJobBuilder);

    protected abstract Object doJoin(M model, JobFlowBuilder jobFlowBuilder);

    protected abstract Object doJoin(M model, FlowBuilder.TransitionBuilder<FlowJobBuilder> transitionBuilder);
}
