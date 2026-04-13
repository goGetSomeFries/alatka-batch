package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;

/**
 * 组件接口，使用{@link JobBuilder}通过{@link ComponentModel}构建{@link Job}
 *
 * @author whocares
 * @see StepComponent
 * @see FlowComponent
 * @see SplitComponent
 * @see DecisionComponent
 */
public interface IComponent {

    /**
     * 根据各类型{@link ComponentModel}和SpringBatch各类builder构造{@link Job}内部结构
     *
     * @param model   {@link ComponentModel}类型
     * @param builder SpringBatch各类builder
     * @return builder
     */
    Object join(ComponentModel model, Object builder);

    /**
     * 根据不同类型{@link ComponentModel}，判断{@link IComponent}子类实例是否匹配
     *
     * @param model {@link ComponentModel}类型
     * @return {@link IComponent}子类实例是否匹配
     */
    boolean matched(ComponentModel model);

    /**
     * 通过{@link SimpleJobBuilder}或{@link JobFlowBuilder}构造{@link Job}
     *
     * @param builder {@link SimpleJobBuilder} or {@link JobFlowBuilder}
     * @return {@link Job}
     */
    static Job build(Object builder) {
        if (builder.getClass() == SimpleJobBuilder.class) {
            SimpleJobBuilder simpleJobBuilder = (SimpleJobBuilder) builder;
            return simpleJobBuilder.build();
        }
        if (builder.getClass() == JobFlowBuilder.class) {
            JobFlowBuilder jobFlowBuilder = (JobFlowBuilder) builder;
            return jobFlowBuilder.end().build();
        }
        throw new IllegalArgumentException("Unsupported builder type: " + builder.getClass());
    }

}
