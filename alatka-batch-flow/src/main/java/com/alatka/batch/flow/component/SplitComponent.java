package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.SplitModel;
import com.alatka.batch.infra.util.ApplicationContextUtil;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.core.task.TaskExecutor;

public class SplitComponent extends AbstractComponent<SplitModel> {

    @Override
    protected Object doTest(SplitModel model, JobBuilder jobBuilder) {
        return null;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doTest(SplitModel model, SimpleJobBuilder simpleJobBuilder) {
        TaskExecutor taskExecutor = ApplicationContextUtil.getBean(model.getTaskExecutor(), TaskExecutor.class);
        Flow[] flows = model.getFlows().stream()
                .map(flowModel -> ApplicationContextUtil.getBean(flowModel.getName(), Flow.class)).toArray(Flow[]::new);

        return simpleJobBuilder.split(taskExecutor).add(flows);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doTest(SplitModel model, JobFlowBuilder jobFlowBuilder) {
        TaskExecutor taskExecutor = ApplicationContextUtil.getBean(model.getTaskExecutor(), TaskExecutor.class);
        Flow[] flows = model.getFlows().stream()
                .map(flowModel -> ApplicationContextUtil.getBean(flowModel.getName(), Flow.class)).toArray(Flow[]::new);
        return jobFlowBuilder.split(taskExecutor).add(flows);
    }

    @Override
    public boolean matched(ComponentModel model) {
        return model.getClass() == SplitModel.class;
    }
}
