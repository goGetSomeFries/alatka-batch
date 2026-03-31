package com.alatka.batch.flow.component;

import com.alatka.batch.flow.FlowAutoConfiguration;
import com.alatka.batch.flow.model.SplitModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.core.task.TaskExecutor;

import java.util.function.BiFunction;

public class SplitComponent extends AbstractComponent<SplitModel> {

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobBuilder jobBuilder) {
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
        return this.apply(model, (taskExecutor, flows) -> jobBuilder.start(passthroughStep).split(taskExecutor).add(flows));
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, SimpleJobBuilder simpleJobBuilder) {
        return this.apply(model, (taskExecutor, flows) -> simpleJobBuilder.split(taskExecutor).add(flows));
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(SplitModel model, JobFlowBuilder jobFlowBuilder) {
        return this.apply(model, (taskExecutor, flows) -> jobFlowBuilder.split(taskExecutor).add(flows));
    }

    private FlowBuilder<FlowJobBuilder> apply(SplitModel model,
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
