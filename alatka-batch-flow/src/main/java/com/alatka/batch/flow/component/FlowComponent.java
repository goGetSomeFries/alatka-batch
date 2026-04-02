package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.FlowModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.DirectFieldAccessor;

import java.util.List;

public class FlowComponent extends AbstractComponent<FlowModel> {

    @Override
    protected JobFlowBuilder doJoin(FlowModel model, JobBuilder jobBuilder) {
        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        return jobBuilder.start(flow);
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(FlowModel model, SimpleJobBuilder simpleJobBuilder) {
        // get last step
        DirectFieldAccessor accessor = new DirectFieldAccessor(simpleJobBuilder);
        List<Step> list = (List<Step>) accessor.getPropertyValue("steps");
        if (list == null) {
            throw new IllegalStateException("Steps list is null");
        }
        Step lastStep = list.get(list.size() - 1);

        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        return simpleJobBuilder
                .on(ExitStatus.COMPLETED.getExitCode()).to(flow)
                .from(lastStep)
                .on("*").fail()
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
