package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;

public interface IComponent {

    boolean matched(ComponentModel model);

    void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory);
}
