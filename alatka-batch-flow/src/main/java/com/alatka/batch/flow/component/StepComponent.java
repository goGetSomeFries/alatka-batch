package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.StepModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;

public class StepComponent extends AbstractComponent<StepModel> {

    @Override
    protected SimpleJobBuilder doJoin(StepModel model, JobBuilder jobBuilder) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        return jobBuilder.start(step);
    }

    @Override
    protected SimpleJobBuilder doJoin(StepModel model, SimpleJobBuilder simpleJobBuilder) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        return simpleJobBuilder.next(step);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(StepModel model, JobFlowBuilder jobFlowBuilder) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        return jobFlowBuilder.next(step);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(StepModel model, FlowBuilder.TransitionBuilder<FlowJobBuilder> transitionBuilder) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        return transitionBuilder.to(step);
    }

    @Override
    protected Class<StepModel> modelClass() {
        return StepModel.class;
    }
}
