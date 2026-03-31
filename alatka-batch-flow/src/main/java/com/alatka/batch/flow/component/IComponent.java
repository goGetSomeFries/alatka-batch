package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;
import org.springframework.batch.core.Job;

public interface IComponent {

    Object join(ComponentModel model, Object builder);

    Job build(Object builder);

    boolean matched(ComponentModel model);

}
