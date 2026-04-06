package com.alatka.batch.flow.component;

import com.alatka.batch.flow.builder.AbstractFlowBuilder;
import com.alatka.batch.flow.model.DecisionModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.concurrent.atomic.AtomicReference;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, JobBuilder jobBuilder) {
        Step passthroughStep = this.createPassthroughStep();
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        FlowBuilder<FlowJobBuilder> finalBuilder = jobBuilder.start(passthroughStep)
                .on(ExitStatus.COMPLETED.getExitCode()).to(decider)
                .from(passthroughStep).on("*").fail();
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = finalBuilder.from(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder).from(decider));
        return finalBuilder;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, SimpleJobBuilder simpleJobBuilder) {
        Step lastStep = this.getLastStep(simpleJobBuilder);
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);

        FlowBuilder<FlowJobBuilder> finalBuilder = simpleJobBuilder
                .on(ExitStatus.COMPLETED.getExitCode()).to(decider)
                .from(lastStep).on("*").fail();
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = finalBuilder.from(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder).from(decider));
        return finalBuilder;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, JobFlowBuilder jobFlowBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        Step passthroughStep = this.createPassthroughStep();
        FlowBuilder<FlowJobBuilder> finalBuilder = jobFlowBuilder.next(passthroughStep);
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = finalBuilder.next(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder).from(decider));
        return finalBuilder;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, FlowBuilder.TransitionBuilder<FlowJobBuilder> transitionBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        FlowBuilder<FlowJobBuilder> finalBuilder = transitionBuilder.to(decider);
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = finalBuilder.from(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder).from(decider));
        return finalBuilder;
    }

    private FlowBuilder<FlowJobBuilder> execute(DecisionModel.InnerModel model,
                                                FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder) {
        DecisionModel.InnerModel.Exit exit = model.getExit();
        if (exit != null) {
            switch (exit) {
                case end:
                    return builder.on(model.getWhen()).end();
                case failed:
                    return builder.on(model.getWhen()).fail();
                case stopped:
                    return builder.on(model.getWhen()).stop();
            }
        }
        return this.doExecute(model, builder);
    }

    private FlowBuilder<FlowJobBuilder> doExecute(DecisionModel.InnerModel model,
                                                  FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder) {
        AtomicReference<Object> reference = new AtomicReference<>(builder.on(model.getWhen()));
        AbstractFlowBuilder.buildComponents(model.getTo(), reference, applicationContext);

        return (FlowBuilder<FlowJobBuilder>) reference.get();
    }

}
