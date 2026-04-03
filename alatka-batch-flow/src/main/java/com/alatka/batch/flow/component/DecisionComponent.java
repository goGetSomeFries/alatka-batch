package com.alatka.batch.flow.component;

import com.alatka.batch.flow.FlowAutoConfiguration;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DecisionComponent extends AbstractComponent<DecisionModel> {

    @Override
    protected Class<DecisionModel> modelClass() {
        return DecisionModel.class;
    }

    @Override
    protected JobFlowBuilder doJoin(DecisionModel model, JobBuilder jobBuilder) {
        Step passthroughStep = applicationContext.getBean(FlowAutoConfiguration.STEP_PASSTHROUGH, Step.class);
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = jobBuilder.start(passthroughStep)
                .on(ExitStatus.COMPLETED.getExitCode()).to(decider)
                .from(passthroughStep).on("*").fail()
                .from(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder, decider));
        return builder;
    }

    @Override
    protected JobFlowBuilder doJoin(DecisionModel model, SimpleJobBuilder simpleJobBuilder) {
        Step lastStep = this.getLastStep(simpleJobBuilder);
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);

        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = simpleJobBuilder
                .on(ExitStatus.COMPLETED.getExitCode()).to(decider)
                .from(lastStep).on("*").fail()
                .from(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder, decider));
        return builder;
    }

    @Override
    protected FlowBuilder<FlowJobBuilder> doJoin(DecisionModel model, JobFlowBuilder jobFlowBuilder) {
        JobExecutionDecider decider = applicationContext.getBean(model.getName(), JobExecutionDecider.class);
        FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder = jobFlowBuilder.next(decider);

        model.getDecisions().forEach(innerModel -> this.execute(innerModel, builder, decider));
        return builder;
    }

    private void execute(DecisionModel.InnerModel model, FlowBuilder.UnterminatedFlowBuilder<FlowJobBuilder> builder, JobExecutionDecider decider) {
        DecisionModel.InnerModel.Exit exit = model.getExit();
        if (exit != null) {
            switch (exit) {
                case end:
                    builder.on(model.getWhen()).end().from(decider);
                    break;
                case failed:
                    builder.on(model.getWhen()).fail().from(decider);
                    break;
                case stopped:
                    builder.on(model.getWhen()).stop().from(decider);
                    break;
            }
            return;
        }

        ArrayList<Map<RootModel.Type, Map<String, Object>>> list = new ArrayList<>(model.getTo());
        AtomicReference<Object> reference = new AtomicReference<>(builder);
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
                        reference.set(builder.on(model.getWhen()).to((JobExecutionDecider) bean));
                    } else {
                        throw new IllegalArgumentException("Unknown decision type: " + bean.getClass());
                    }
                });

        this.test(list, reference);
    }

    private void test(List<Map<RootModel.Type, Map<String, Object>>> list, AtomicReference<Object> reference) {
        list.stream().flatMap(map -> map.entrySet().stream()
                        .map(entry -> JsonUtil.convertToObject(entry.getValue(), entry.getKey().getClazz())))
                .forEach(model -> applicationContext.getBeansOfType(IComponent.class).values().stream()
                        .filter(component -> component.matched(model))
                        .findFirst()
                        .ifPresent(component -> {
                            Object builder = component.join(model, reference.get());
                            reference.set(builder);
                        })
                );
    }


}
