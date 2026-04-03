package com.alatka.batch.flow.component;

import com.alatka.batch.flow.FlowAutoConfiguration;
import com.alatka.batch.flow.model.SplitModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.core.task.TaskExecutor;

import java.util.function.BiFunction;

public class SplitComponent extends AbstractComponent<SplitModel> {

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobBuilder jobBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Flow passthroughFlow = applicationContext.getBean(FlowAutoConfiguration.FLOW_PASSTHROUGH, Flow.class);
            return jobBuilder.start(passthroughFlow).split(taskExecutor).add(flows);
        });
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, SimpleJobBuilder simpleJobBuilder) {
        return this.execute(model, (taskExecutor, flows) -> {
            Step lastStep = this.getLastStep(simpleJobBuilder);
            Flow splitFlow = new FlowBuilder<Flow>("splitFlow").split(taskExecutor).add(flows).end();
            return simpleJobBuilder
                    .on(ExitStatus.COMPLETED.getExitCode()).to(splitFlow)
                    .from(lastStep).on("*").fail()
                    .from(splitFlow);
        });
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobFlowBuilder jobFlowBuilder) {
        return this.execute(model, (taskExecutor, flows) -> jobFlowBuilder.split(taskExecutor).add(flows));
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
