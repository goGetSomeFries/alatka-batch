package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.DecisionModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.concurrent.atomic.AtomicReference;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }

    @Override
    protected Object doJoin(DecisionModel model, JobBuilder jobBuilder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected FlowBuilder doJoin(DecisionModel model, SimpleJobBuilder simpleJobBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        JobFlowBuilder builder = simpleJobBuilder.next(decider);

        AtomicReference<FlowBuilder> reference = new AtomicReference<>(builder);
        model.getDecisions().forEach(innerModel -> {
            Object bean = applicationContext.getBean(innerModel.getTo());
            reference.get().from(decider).on(innerModel.getOn()).to((Step) null);
        });
        return null;
    }

    @Override
    protected Object doJoin(DecisionModel model, JobFlowBuilder jobFlowBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        return jobFlowBuilder.next(decider);
    }

}
