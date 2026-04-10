package com.alatka.batch.flow.builder;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;

/**
 * {@link Job}构造器，动态构建并注册到{@link JobRegistry}中
 */
public interface FlowBuilder {

    /**
     * 构建所有{@link Job}
     */
    void build();

    /**
     * 构建指定{@link Job}
     *
     * @param identity {@link Job}唯一标识
     */
    void build(String identity);
}
