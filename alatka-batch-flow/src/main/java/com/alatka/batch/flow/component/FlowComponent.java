package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.FlowModel;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;

public class FlowComponent extends AbstractComponent<FlowModel> {

    @Override
    protected Wrapper doJoin(FlowModel model, JobBuilder jobBuilder, Object lastOne) {
        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        JobFlowBuilder builder = jobBuilder.start(flow);
        return new Wrapper(builder, flow);
    }

    @Override
    protected Wrapper doJoin(FlowModel model, SimpleJobBuilder simpleJobBuilder, Object lastOne) {
        if (lastOne == null) {
            throw new IllegalArgumentException("lastOne must not be null");
        }

        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        FlowBuilder<FlowJobBuilder> builder = simpleJobBuilder.on(ExitStatus.COMPLETED.getExitCode()).to(flow)
                .from((Step) lastOne).on("*").fail();
        return new Wrapper(builder, flow);
    }

    @Override
    protected Wrapper doJoin(FlowModel model, JobFlowBuilder jobFlowBuilder, Object lastOne) {
        if (lastOne == null) {
            throw new IllegalArgumentException("lastOne must not be null");
        }
        if (lastOne instanceof Step) {
            jobFlowBuilder.from((Step) lastOne);
        } else if (lastOne instanceof Flow) {
            jobFlowBuilder.from((Flow) lastOne);
        } else {
            throw new IllegalArgumentException("lastOne must be Step or Flow, current: " + lastOne);
        }
        Flow flow = applicationContext.getBean(model.getName(), Flow.class);
        FlowBuilder<FlowJobBuilder> builder = jobFlowBuilder.next(flow);
        return new Wrapper(builder, flow);
    }

    @Override
    protected Class<FlowModel> modelClass() {
        return FlowModel.class;
    }

}
