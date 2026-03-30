package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import com.alatka.batch.flow.model.FlowModel;
import com.alatka.batch.infra.util.ApplicationContextUtil;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;

public class FlowComponent extends AbstractComponent<FlowModel> {

    @Override
    protected JobFlowBuilder doTest(FlowModel model, JobBuilder jobBuilder) {
        Flow flow = ApplicationContextUtil.getBean(model.getName(), Flow.class);
        return jobBuilder.start(flow);
    }

    @Override
    protected Object doTest(FlowModel model, SimpleJobBuilder simpleJobBuilder) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doTest(FlowModel model, JobFlowBuilder jobFlowBuilder) {
        Flow flow = ApplicationContextUtil.getBean(model.getName(), Flow.class);
        return jobFlowBuilder.next(flow);
    }

    @Override
    public boolean matched(ComponentModel model) {
        return model.getClass() == FlowModel.class;
    }
}
