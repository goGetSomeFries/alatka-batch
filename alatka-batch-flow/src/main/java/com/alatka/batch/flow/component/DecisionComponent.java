package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.DecisionModel;
import com.alatka.batch.infra.util.ApplicationContextUtil;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Object doTest(DecisionModel model, JobBuilder jobBuilder) {
        return null;
    }

    @Override
    protected Object doTest(DecisionModel model, SimpleJobBuilder simpleJobBuilder) {
        JobExecutionDecider decider = ApplicationContextUtil.getBean(model.getName(), JobExecutionDecider.class);
        return simpleJobBuilder.next(decider);
    }

    @Override
    protected Object doTest(DecisionModel model, JobFlowBuilder jobFlowBuilder) {
        JobExecutionDecider decider = ApplicationContextUtil.getBean(model.getName(), JobExecutionDecider.class);
        return jobFlowBuilder.next(decider);
    }

    @Override
    public boolean matched(ComponentModel model) {
        return model.getClass() == DecisionModel.class;
    }
}
