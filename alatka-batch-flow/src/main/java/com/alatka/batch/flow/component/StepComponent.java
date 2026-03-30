package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.StepModel;
import com.alatka.batch.infra.util.ApplicationContextUtil;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;

public class StepComponent extends AbstractComponent<StepModel> {

    @Override
    protected SimpleJobBuilder doTest(StepModel model, JobBuilder jobBuilder) {
        Step step = ApplicationContextUtil.getBean(model.getName(), Step.class);
        return jobBuilder.start(step);
    }

    @Override
    protected SimpleJobBuilder doTest(StepModel model, SimpleJobBuilder simpleJobBuilder) {
        Step step = ApplicationContextUtil.getBean(model.getName(), Step.class);
        return simpleJobBuilder.next(step);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doTest(StepModel model, JobFlowBuilder jobFlowBuilder) {
        Step step = ApplicationContextUtil.getBean(model.getName(), Step.class);
        return jobFlowBuilder.next(step);
    }

    @Override
    public boolean matched(ComponentModel model) {
        return model.getClass() == StepModel.class;
    }

}
