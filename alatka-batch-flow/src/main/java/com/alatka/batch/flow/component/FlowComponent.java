package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.FlowModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;

public class FlowComponent extends AbstractComponent<FlowModel> {

    @Override
    protected JobFlowBuilder doJoin(FlowModel model, JobBuilder jobBuilder) {
        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        return jobBuilder.start(flow);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(FlowModel model, SimpleJobBuilder simpleJobBuilder) {
        Step lastStep = this.getLastStep(simpleJobBuilder);

        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        return simpleJobBuilder
                .on(ExitStatus.COMPLETED.getExitCode()).to(flow)
                .from(lastStep).on("*").fail()
                .from(flow);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(FlowModel model, JobFlowBuilder jobFlowBuilder) {
        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        return jobFlowBuilder.next(flow);
    }

    @Override
    protected Class<FlowModel> modelClass() {
        return FlowModel.class;
    }

}
