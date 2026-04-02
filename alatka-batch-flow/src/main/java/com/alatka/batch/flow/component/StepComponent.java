package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.StepModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;

public class StepComponent extends AbstractComponent<StepModel> {

    @Override
    protected Wrapper doJoin(StepModel model, JobBuilder jobBuilder, Object lastOne) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        SimpleJobBuilder builder = jobBuilder.start(step);
        return new Wrapper(builder, step);
    }

    @Override
    protected Wrapper doJoin(StepModel model, SimpleJobBuilder simpleJobBuilder, Object lastOne) {
        Step step = applicationContext.getBean(model.getName(), Step.class);
        SimpleJobBuilder builder = simpleJobBuilder.next(step);
        return new Wrapper(builder, step);
    }

    @Override
    protected Wrapper doJoin(StepModel model, JobFlowBuilder jobFlowBuilder, Object lastOne) {
        if (lastOne == null) {
            throw new IllegalArgumentException("lastOne must not be null");
        }
        if (lastOne instanceof Step) {
            jobFlowBuilder.from((Step) lastOne);
        } else if (lastOne instanceof Flow) {
            jobFlowBuilder.from((Flow) lastOne);
        } else {
            throw new IllegalArgumentException("lastOne must be Step or Flow, current: " + lastOne);
        }
        Step step = applicationContext.getBean(model.getName(), Step.class);
        FlowBuilder<FlowJobBuilder> builder = jobFlowBuilder.next(step);
        return new Wrapper(builder, step);
    }

    @Override
    protected Class<StepModel> modelClass() {
        return StepModel.class;
    }
}
