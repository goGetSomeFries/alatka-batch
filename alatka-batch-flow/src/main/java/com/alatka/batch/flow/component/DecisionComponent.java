package com.alatka.batch.flow.component;

import com.alatka.batch.flow.FlowAutoConfiguration;
import com.alatka.batch.flow.model.DecisionModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }

    @Override
    protected FlowJobBuilder doJoin(DecisionModel model, JobBuilder jobBuilder) {
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        JobFlowBuilder builder = jobBuilder.start(passthroughStep).next(decider);

        model.getDecisions().forEach(innerModel -> this.apply(innerModel, builder, decider));
        return builder.end();
    }

    @Override
    protected FlowJobBuilder doJoin(DecisionModel model, SimpleJobBuilder simpleJobBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        JobFlowBuilder builder = simpleJobBuilder.next(decider);

        model.getDecisions().forEach(innerModel -> this.apply(innerModel, builder, decider));
        return builder.end();
    }

    @Override
    protected FlowJobBuilder doJoin(DecisionModel model, JobFlowBuilder jobFlowBuilder) {
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        FlowBuilder<FlowJobBuilder> builder = jobFlowBuilder.next(decider)
                .on(FlowAutoConfiguration.STEP_PASSTHROUGH).to(passthroughStep);

        model.getDecisions().forEach(innerModel -> this.apply(innerModel, builder, decider));
        return builder.end();
    }

    private void apply(DecisionModel.InnerModel model, FlowBuilder<FlowJobBuilder> builder, JobExecutionDecider decider) {
        builder.from(decider);
        try {
            DecisionModel.InnerModel.Status status = DecisionModel.InnerModel.Status.valueOf(model.getTo());
            switch (status) {
                case end:
                    builder.on(model.getOn()).end();
                    break;
                case failed:
                    builder.on(model.getOn()).fail();
                    break;
                case stopped:
                    builder.on(model.getOn()).stop();
                    break;
            }
        } catch (IllegalArgumentException e) {
            Object bean = applicationContext.getBean(model.getTo());
            if (bean instanceof Step) {
                builder.on(model.getOn()).to((Step) bean);
            } else if (bean instanceof Flow) {
                builder.on(model.getOn()).to((Flow) bean);
            } else if (bean instanceof JobExecutionDecider) {
                builder.on(model.getOn()).to((JobExecutionDecider) bean);
            } else {
                throw new IllegalArgumentException("Unknown decision type: " + bean.getClass());
            }
        }

    }

}
