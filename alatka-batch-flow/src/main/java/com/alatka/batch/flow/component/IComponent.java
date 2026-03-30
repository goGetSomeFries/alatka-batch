package com.alatka.batch.flow.component;

import com.alatka.batch.flow.model.ComponentModel;

public interface IComponent {

    Object test(ComponentModel model, Object builder);

    boolean matched(ComponentModel model);

}
