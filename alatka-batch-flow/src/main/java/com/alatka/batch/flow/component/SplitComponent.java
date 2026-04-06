package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.SplitModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.function.BiFunction;

public class SplitComponent extends AbstractComponent<SplitModel> {

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobBuilder jobBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Flow[] array = Arrays.copyOfRange(flows, 1, flows.length);
            return jobBuilder.start(flows[0]).split(taskExecutor).add(array);
        });
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, SimpleJobBuilder simpleJobBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Step lastStep = this.getLastStep(simpleJobBuilder);
            Flow splitFlow =
                    this.createFlow("splitFlow", flowBuilder -> flowBuilder.split(taskExecutor).add(flows).end());
            return simpleJobBuilder
                    .on(ExitStatus.COMPLETED.getExitCode()).to(splitFlow)
                    .from(lastStep).on("*").fail()
                    .from(splitFlow);
        });
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobFlowBuilder jobFlowBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Flow splitFlow =
                    this.createFlow("splitFlow", flowBuilder -> flowBuilder.split(taskExecutor).add(flows).end());
            return jobFlowBuilder.next(splitFlow);
        });
    }

    @Override
    protected Object doJoin(SplitModel model, FlowBuilder.TransitionBuilder<FlowJobBuilder> transitionBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Flow splitFlow =
                    this.createFlow("splitFlow", flowBuilder -> flowBuilder.split(taskExecutor).add(flows).end());
            return transitionBuilder.to(splitFlow);
        });
    }

    private FlowBuilder<FlowJobBuilder> execute(SplitModel model,
                                                BiFunction<TaskExecutor, Flow[], FlowBuilder<FlowJobBuilder>> biFunction) {
        TaskExecutor taskExecutor = applicationContext.getBean(model.getTaskExecutor(), TaskExecutor.class);
        Flow[] flows = model.getFlows().stream()
                .map(flowModel -> applicationContext.getBean(flowModel.getName(), Flow.class)).toArray(Flow[]::new);
        return biFunction.apply(taskExecutor, flows);
    }

    @Override
    protected Class<SplitModel> modelClass() {
        return SplitModel.class;
    }
}
