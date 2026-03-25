package com.alatka.batch.flow.component;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractComponent implements IComponent {

    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Override
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }
}
