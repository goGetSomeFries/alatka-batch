package com.alatka.batch.flow;

import com.alatka.batch.flow.builder.DatabaseBatchFlowBuilder;
import com.alatka.batch.flow.builder.FileBatchFlowBuilder;
import com.alatka.batch.flow.component.DecisionComponent;
import com.alatka.batch.flow.component.FlowComponent;
import com.alatka.batch.flow.component.SplitComponent;
import com.alatka.batch.flow.component.StepComponent;
import com.alatka.batch.flow.config.FlowProperties;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
@EnableConfigurationProperties(FlowProperties.class)
@ConditionalOnProperty(value = "alatka.batch.flow.enabled", havingValue = "true", matchIfMissing = true)
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    @ConditionalOnMissingBean(FileBatchFlowBuilder.class)
    @ConditionalOnProperty(value = "alatka.batch.flow.type", havingValue = "yaml", matchIfMissing = true)
    public FileBatchFlowBuilder fileFlowBuilder(FlowProperties properties) {
        FileBatchFlowBuilder builder = new FileBatchFlowBuilder();
        String classpath = properties.getClasspath();
        if (StringUtils.hasText(classpath)) {
            builder.setClasspath(classpath);
        }
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean(DatabaseBatchFlowBuilder.class)
    @ConditionalOnProperty(value = "alatka.batch.flow.type", havingValue = "database")
    public DatabaseBatchFlowBuilder databaseFlowBuilder(DataSource dataSource) {
        DatabaseBatchFlowBuilder builder = new DatabaseBatchFlowBuilder();
        builder.setDataSource(dataSource);
        return builder;
    }

    @Bean
    public StepComponent stepComponent() {
        return new StepComponent();
    }

    @Bean
    public FlowComponent flowComponent() {
        return new FlowComponent();
    }

    @Bean
    public DecisionComponent decisionComponent() {
        return new DecisionComponent();
    }

    @Bean
    public SplitComponent splitComponent() {
        return new SplitComponent();
    }

}
