package com.alatka.batch.flow.component;

import com.alatka.batch.flow.FlowAutoConfiguration;
import com.alatka.batch.flow.builder.AbstractFlowBuilder;
import com.alatka.batch.flow.model.BeanComponentModel;
import com.alatka.batch.flow.model.DecisionModel;
import com.alatka.batch.flow.model.RootModel;
import com.alatka.batch.infra.util.JsonUtil;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.*;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, JobBuilder jobBuilder) {
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
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
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
        FlowBuilder<FlowJobBuilder> finalBuilder = jobFlowBuilder.next(passthroughStep);
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = finalBuilder.next(decider);

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

    private FlowBuilder<FlowJobBuilder> doExecute(DecisionModel.InnerModel model, FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder) {
        ArrayList<Map<RootModel.Type, Map<String, Object>>> list = new ArrayList<>(model.getTo());
        AtomicReference<Object> reference = new AtomicReference<>();
        list.remove(0).entrySet().stream()
                .map(entry -> JsonUtil.convertToObject(entry.getValue(), entry.getKey().getClazz()))
                .map(BeanComponentModel.class::cast)
                .forEach(componentModel -> {
                    Object bean = applicationContext.getBean(componentModel.getName());
                    if (bean instanceof Step) {
                        reference.set(builder.on(model.getWhen()).to((Step) bean));
                    } else if (bean instanceof Flow) {
                        reference.set(builder.on(model.getWhen()).to((Flow) bean));
                    } else if (bean instanceof JobExecutionDecider) {
                        // TODO 未解析DecisionModel
                        reference.set(builder.on(model.getWhen()).to((JobExecutionDecider) bean));
                    } else {
                        throw new IllegalArgumentException("Unknown decision type: " + bean.getClass());
                    }
                });

        AbstractFlowBuilder.buildList(list, reference, applicationContext);

        return (FlowBuilder<FlowJobBuilder>) reference.get();
    }

}
