package com.alatka.batch.example.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TaskExecutorConfig {

    @Bean
    public TaskExecutor defaultTaskExecutor() {
        return new SyncTaskExecutor();
    }
}
