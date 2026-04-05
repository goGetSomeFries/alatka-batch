package com.alatka.batch.flow;

import com.alatka.batch.flow.builder.DatabaseFlowBuilder;
import com.alatka.batch.flow.builder.FileFlowBuilder;
import com.alatka.batch.flow.component.DecisionComponent;
import com.alatka.batch.flow.component.FlowComponent;
import com.alatka.batch.flow.component.SplitComponent;
import com.alatka.batch.flow.component.StepComponent;
import com.alatka.batch.flow.config.FlowProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FlowProperties.class)
@ConditionalOnProperty(value = "alatka.batch.flow.enabled", havingValue = "true", matchIfMissing = true)
public class FlowAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FileFlowBuilder.class)
    @ConditionalOnProperty(value = "alatka.batch.flow.type", havingValue = "yaml", matchIfMissing = true)
    public FileFlowBuilder fileFlowBuilder(FlowProperties properties) {
        FileFlowBuilder builder = new FileFlowBuilder();
        String classpath = properties.getClasspath();
        if (StringUtils.hasText(classpath)) {
            builder.setClasspath(classpath);
        }
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean(DatabaseFlowBuilder.class)
    @ConditionalOnProperty(value = "alatka.batch.flow.type", havingValue = "database")
    public DatabaseFlowBuilder databaseFlowBuilder(DataSource dataSource) {
        DatabaseFlowBuilder builder = new DatabaseFlowBuilder();
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

    public static final String FLOW_PASSTHROUGH = "passthroughFlow";

}
